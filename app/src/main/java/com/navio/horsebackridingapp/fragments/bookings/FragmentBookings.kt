package com.navio.horsebackridingapp.fragments.bookings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.navio.horsebackridingapp.api.RetrofitClient
import com.navio.horsebackridingapp.data.Booking
import com.navio.horsebackridingapp.data.BookingUpdate
import com.navio.horsebackridingapp.data.Horse
import com.navio.horsebackridingapp.databinding.FragmentBookingsBinding
import com.navio.horsebackridingapp.dialogs.DialogEditBooking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentBookings : Fragment() {

    private lateinit var binding: FragmentBookingsBinding
    private lateinit var recycler: RecyclerView
    private lateinit var adapterBookings: AdapterBookings
    private var bookingsLiveData = MutableLiveData<MutableList<Booking>>()
    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentBookingsBinding.inflate(layoutInflater)

        retrieveData()
        initAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    private fun retrieveData() {
        lifecycleScope.launch(Dispatchers.IO) {
            fetchBookings()
        }
    }

    private fun initAdapter() {

        lifecycleScope.launch(Dispatchers.Main) {

            recycler = binding.fragmentBookingsRecycler
            adapterBookings = AdapterBookings(

                object : FragmentBookings.OnItemChanges {
                    //Edit
                    override fun onItemEdited(booking: Booking) {

                        lifecycleScope.launch {
                            val dialogEditBooking =
                                DialogEditBooking(requireContext(), fetchHorses(),
                                    object : OnItemEdited {
                                        override fun onItemEdited(booking: BookingUpdate) {
                                            lifecycleScope.launch(Dispatchers.IO) {
                                                val apiService =
                                                    RetrofitClient.getInstance(requireContext())
                                                val response =
                                                    apiService.updateBookingAPI(booking.id, booking)

                                                if (response.isSuccessful) {
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Booking updated.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    fetchBookings()
                                                } else {
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Error updating booking.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                }
                                            }
                                        }
                                    }
                                )
                            dialogEditBooking.create(booking)
                            dialogEditBooking.show()
                        }
                    }

                    //Delete
                    override fun onItemDeleted(id: Int) {

                        lifecycleScope.launch(Dispatchers.IO) {
                            val apiService = RetrofitClient.getInstance(requireContext())
                            val response = apiService.deleteBookingAPI(id)

                            if (response.isSuccessful) {
                                Log.d("Navio_log", "Booking deleted")
                                fetchBookings()
                            } else {
                                Log.d("Navio_log", "Error deleting booking")
                            }
                        }
                    }
                }

            )
            recycler.adapter = adapterBookings
            recycler.layoutManager = LinearLayoutManager(context)

            // Observe the LiveData object and update the adapter when the bookings change
            bookingsLiveData.observe(viewLifecycleOwner) { bookings ->
                adapterBookings.updateBookings(bookings)
            }
        }
    }

    private suspend fun fetchBookings() {

        val horses: List<Horse> = fetchHorses()
        val apiService = RetrofitClient.getInstance(requireContext())
        val response = apiService.getAllBookings()

        val jsonResponse = response.body()?.string()

        //Parse to a list of bookings
        val gson = Gson()
        val bookingListType = object : TypeToken<List<Booking>>() {}.type
        val bookings: List<Booking> = gson.fromJson(jsonResponse, bookingListType)
        bookings.forEach { booking ->
            val horse = horses.find { it.id == booking.horseId }
            booking.horseName = horse?.name ?: "Unknown"
        }
        //Get the user id
        userId = bookings.first()?.let {
            it.userId
        } ?: -1

        bookingsLiveData.postValue(bookings.toMutableList())
    }

    private suspend fun fetchHorses(): List<Horse> {
        val apiService = RetrofitClient.getInstance(requireContext())
        val response = apiService.getAllHorses()

        val jsonResponse = response.body()?.string()

        //Parse to a list of horses
        val gson = Gson()
        val horseListType = object : TypeToken<List<Horse>>() {}.type
        val horses: List<Horse> = gson.fromJson(jsonResponse, horseListType)

        return horses
    }

    interface OnItemChanges {
        fun onItemDeleted(id: Int)
        fun onItemEdited(booking: Booking)
    }

    interface OnItemEdited {
        fun onItemEdited(booking: BookingUpdate)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentBookings()
    }
}