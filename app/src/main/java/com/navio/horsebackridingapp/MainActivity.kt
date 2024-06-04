package com.navio.horsebackridingapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.navio.horsebackridingapp.databinding.ActivityMainBinding
import com.navio.horsebackridingapp.fragments.FragmentLogin
import com.navio.horsebackridingapp.fragments.bookings.FragmentBook
import com.navio.horsebackridingapp.fragments.bookings.FragmentBookings

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtons()
        enableAdd()
        attachLoginFragment()
    }

    private fun setButtons() {

        binding.mainActivityButtonAdd.setOnClickListener {
            attachBookFragment()
        }

        binding.mainActivityButtonBookings.setOnClickListener {
            attachBookingsFragment()
        }
    }

    private fun attachLoginFragment() {
    val loginFragment = FragmentLogin.newInstance()
    loginFragment.setLoginCallback(object : LoginCallback {
        override fun onLoginSuccess() {
            attachBookingsFragment()
        }
    })

    supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace(binding.mainActivityContainer.id, loginFragment)
    }
}

    private fun attachBookingsFragment() {

        enableBookings()

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(binding.mainActivityContainer.id, FragmentBookings.newInstance())
        }
    }

    private fun attachBookFragment() {

        enableAdd()

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(binding.mainActivityContainer.id, FragmentBook.newInstance())
        }
    }

    //Enable/Disable buttons
    private fun enableBookings() {
        binding.mainActivityButtonAdd.visibility = View.VISIBLE
        binding.mainActivityButtonBookings.visibility = View.GONE
    }

    private fun enableAdd() {
        binding.mainActivityButtonAdd.visibility = View.GONE
        binding.mainActivityButtonBookings.visibility = View.VISIBLE
    }

    interface LoginCallback {
        fun onLoginSuccess()
    }

}