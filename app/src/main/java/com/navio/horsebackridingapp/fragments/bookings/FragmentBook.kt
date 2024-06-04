package com.navio.horsebackridingapp.fragments.bookings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.navio.horsebackridingapp.api.RetrofitClient
import com.navio.horsebackridingapp.data.Booking
import com.navio.horsebackridingapp.data.Horse
import com.navio.horsebackridingapp.databinding.FragmentBookBinding

class FragmentBook : Fragment() {

    private lateinit var binding: FragmentBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBookBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentBook()
    }
}