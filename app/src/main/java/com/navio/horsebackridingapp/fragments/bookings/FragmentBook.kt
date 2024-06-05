package com.navio.horsebackridingapp.fragments.bookings

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.navio.horsebackridingapp.api.RetrofitClient
import com.navio.horsebackridingapp.data.BookingRequest
import com.navio.horsebackridingapp.data.Horse
import com.navio.horsebackridingapp.databinding.FragmentBookBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class FragmentBook() : Fragment() {

    private lateinit var binding: FragmentBookBinding
    private var horses = emptyList<Horse>()
    private var spinnerHorseIds = listOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBookBinding.inflate(layoutInflater)

        lifecycleScope.launch(Dispatchers.IO) {
            horses = fetchHorses()
            withContext(Dispatchers.Main) {
                initSpinner()
            }
        }

        //Calendar settings
        binding.fragmentBookFieldDate.isClickable = false
        binding.fragmentBookFieldDate.isCursorVisible = false
        binding.fragmentBookFieldDate.isFocusable = false
        binding.fragmentBookFieldDate.isFocusableInTouchMode = false
        //Launch calender
        binding.fragmentBookFieldDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener { selection ->
                val selectedDate =
                    Instant.ofEpochMilli(selection).atZone(ZoneId.systemDefault()).toLocalDate()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val formattedDate = selectedDate.format(formatter)
                binding.fragmentBookFieldDate.setText(formattedDate)
            }
            datePicker.show(childFragmentManager, "Date")
        }

        //Launch hour picker
        val spinner: Spinner = binding.fragmentBookFieldHour
        val hours = listOf(10, 11, 12, 13)
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, hours)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        binding.fragmentBookButtonConfirm.setOnClickListener {

            //Checks if the fields are empty
            if (binding.fragmentBookFieldDate.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please fill in all fields needed.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {

                lifecycleScope.launch(Dispatchers.IO) {

                    if (spinnerHorseIds.isEmpty()) {
                        return@launch
                    }

                    val horseId =
                        spinnerHorseIds[binding.fragmentBookSpinnerHorse.selectedItemPosition]
                    val date = binding.fragmentBookFieldDate.text.toString()
                    val hour = spinner.selectedItem.toString().toIntOrNull() ?: 0
                    val comment = binding.fragmentBookFieldComment.text.toString()

                    val newBook = BookingRequest(
                        horseId,
                        date,
                        hour,
                        comment
                    )

                    Log.d("Navio_fdlog", "Booking: $newBook")

                    val apiService = RetrofitClient.getInstance(requireContext())
                    val response = apiService.createBookingAPI(newBook)
                    //If the response is successful, it would return an integer with the booking id
                    if (response.isSuccessful) {

                        val responseString = response.body()?.string()
                        val responseInt = responseString?.toIntOrNull()

                        if (responseInt != null) {
                            withContext(Dispatchers.Main) {
                                binding.fragmentBookFieldDate.text.clear()
                                binding.fragmentBookFieldHour.setSelection(0)
                                binding.fragmentBookFieldComment.text.clear()
                                binding.fragmentBookSpinnerHorse.setSelection(0)
                                Toast.makeText(
                                    requireContext(),
                                    "Booking was created successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                //Send a whatsapp message
                                val mensaje = "Your booking has been confirmed!\n\n" +
                                        "Details:\n" +
                                        "Date: $date\n" +
                                        "Hour: $hour\n"
                                val phoneNumber = 618368854
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data =
                                    Uri.parse(
                                        "https://api.whatsapp.com/send?phone=$phoneNumber&text=${
                                            Uri.encode(
                                                mensaje
                                            )
                                        }"
                                    )
                                startActivity(intent)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "An error occurred.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initSpinner() {
        val spinner: Spinner = binding.fragmentBookSpinnerHorse
        val spinnerHorses = horses.filter { horse -> horse.isSick == 0 }
        spinnerHorseIds = spinnerHorses.map { it.id }
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            spinnerHorses.map { it.name })
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    public suspend fun fetchHorses(): List<Horse> {
        val apiService = RetrofitClient.getInstance(requireContext())
        val response = apiService.getAllHorses()

        val jsonResponse = response.body()?.string()

        //Parse to a list of horses
        val gson = Gson()
        val horseListType = object : TypeToken<List<Horse>>() {}.type
        val horses: List<Horse> = gson.fromJson(jsonResponse, horseListType)

        return horses
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentBook()
    }
}