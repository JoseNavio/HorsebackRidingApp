package com.navio.horsebackridingapp.fragments.bookings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.navio.horsebackridingapp.api.RetrofitClient
import com.navio.horsebackridingapp.data.Booking
import com.navio.horsebackridingapp.data.Horse
import com.navio.horsebackridingapp.databinding.FragmentBookingsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentBookings : Fragment() {

    private lateinit var binding: FragmentBookingsBinding
    private lateinit var recycler: RecyclerView
    private lateinit var adapterBookings: AdapterBookings
    var bookingsLiveData = MutableLiveData<MutableList<Booking>>()

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

    Log.d("Navio_horses", horses.toString())

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

        Log.d("Navio_horse_name", booking.horseName)
    }

    bookingsLiveData.postValue(bookings.toMutableList())
}

    private suspend fun fetchHorses(): List<Horse> {
        val apiService = RetrofitClient.getInstance(requireContext())
        val response = apiService.getAllHorses()

        val jsonResponse = response.body()?.string()

        //Parse to a list of horses
        val gson = Gson()
        val horseListType = object : TypeToken<List<Horse>>() {}.type
        val horses:List<Horse> = gson.fromJson(jsonResponse, horseListType)

        return horses
    }

    interface OnItemChanges {
        fun onItemDeleted(id: Int)
    }
    companion object {
        @JvmStatic
        fun newInstance() = FragmentBookings()
    }
}