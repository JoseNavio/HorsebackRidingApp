package com.navio.horsebackridingapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.navio.horsebackridingapp.R
import com.navio.horsebackridingapp.api.RetrofitClient
import com.navio.horsebackridingapp.data.LoginRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentLogin : Fragment() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        usernameEditText = view.findViewById(R.id.fragment_login_edit_text_username)
        passwordEditText = view.findViewById(R.id.fragment_login_edit_text_password)
        loginButton = view.findViewById(R.id.fragment_login_button_login)

        //It is neccessary to create a network security configuration file so this app can access the site
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            val request = LoginRequest(username, password)

            RetrofitClient.instance.userLogin(request).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    // Handle the response
                    Log.d("Navio_Jose", response.body().toString())

                    //Logs the token received from the server
                    Log.d("Navio_Jose", response.headers().toString())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Handle the failure
                    Log.d("Navio_Mal", t.message.toString())
                }
            })
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentLogin()
    }
}