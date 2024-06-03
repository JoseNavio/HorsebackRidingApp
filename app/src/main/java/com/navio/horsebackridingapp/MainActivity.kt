package com.navio.horsebackridingapp

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.navio.horsebackridingapp.databinding.ActivityMainBinding
import com.navio.horsebackridingapp.fragments.FragmentLogin
import com.navio.horsebackridingapp.fragments.bookings.FragmentBookings

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtons()
        disableButtons()
        attachLoginFragment()
    }

    private fun setButtons() {

        binding.mainActivityButtonLogin.setOnClickListener {
            attachLoginFragment()
        }

        binding.mainActivityButtonBookings.setOnClickListener {
            attachBookingsFragment()
        }
    }

    private fun attachLoginFragment() {
    val loginFragment = FragmentLogin.newInstance()
    loginFragment.setLoginCallback(object : LoginCallback {
        override fun onLoginSuccess() {
            enableButtons()
            attachBookingsFragment()
        }
    })

    supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace(binding.mainActivityContainer.id, loginFragment)
    }
}

    private fun attachBookingsFragment() {

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(binding.mainActivityContainer.id, FragmentBookings.newInstance())
        }
    }

    //Enable/Disable buttons
    private fun enableButtons() {
        binding.mainActivityButtonLogin.visibility = View.VISIBLE
        binding.mainActivityButtonBookings.visibility = View.VISIBLE
    }

    private fun disableButtons() {
        binding.mainActivityButtonLogin.visibility = View.GONE
        binding.mainActivityButtonBookings.visibility = View.GONE
    }
    interface LoginCallback {
        fun onLoginSuccess()
    }
}