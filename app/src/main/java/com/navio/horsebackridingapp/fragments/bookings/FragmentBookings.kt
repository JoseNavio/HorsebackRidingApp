package com.navio.horsebackridingapp.fragments.bookings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.navio.horsebackridingapp.R
import com.navio.horsebackridingapp.data.Booking
import com.navio.horsebackridingapp.databinding.FragmentBookingsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentBookings : Fragment() {


    private lateinit var binding: FragmentBookingsBinding
    private lateinit var recycler: RecyclerView
    private lateinit var adapterBookings: AdapterBookings
    private lateinit var bookings: MutableList<Booking>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentBookingsBinding.inflate(layoutInflater)

        initAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    private fun initAdapter() {

        //todo Test mock
        bookings = mutableListOf(
            Booking("Macarena", "12-07-2023", "12:00", "Estoy..."),
            Booking("Pitufo", "12-08-2023", "11:00", "Mi mujer me ha dejado."),
            Booking("Pablo Motos", "12-09-2023", "10:00", "Estoy en la tele.")
        )

        lifecycleScope.launch(Dispatchers.IO) {
            recycler = binding.fragmentBookingsRecycler
            adapterBookings = AdapterBookings(bookings)
            recycler.adapter = adapterBookings
            recycler.layoutManager = LinearLayoutManager(context)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentBookings()
    }
}