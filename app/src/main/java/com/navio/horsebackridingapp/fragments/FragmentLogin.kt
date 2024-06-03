package com.navio.horsebackridingapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.navio.horsebackridingapp.MainActivity
import com.navio.horsebackridingapp.R
import com.navio.horsebackridingapp.api.RetrofitClient
import com.navio.horsebackridingapp.data.LoginRequest
import com.navio.horsebackridingapp.data.LoginResponse
import com.navio.horsebackridingapp.fragments.bookings.FragmentBookings
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentLogin : Fragment() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    private var callback: MainActivity.LoginCallback? = null
    fun setLoginCallback(callback: MainActivity.LoginCallback) {
        this.callback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return initComponents(inflater.inflate(R.layout.fragment_login, container, false))
    }

    private fun initComponents(view: View): View {

        usernameEditText = view.findViewById(R.id.fragment_login_edit_text_username)
        passwordEditText = view.findViewById(R.id.fragment_login_edit_text_password)
        loginButton = view.findViewById(R.id.fragment_login_button_login)

        //It is neccessary to create a network security configuration file so this app can access the site
        loginButton.setOnClickListener {

            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if(username.isEmpty()){
                usernameEditText.error = "Username required..."
                usernameEditText.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()){
                passwordEditText.error = "Password required..."
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            val request = LoginRequest(username, password)

            RetrofitClient.instance.userLogin(request).enqueue(object : Callback<ResponseBody> {

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    //Save the token in the shared preferences
                    val response = response.body()?.string()

                    if (response != WRONG_LOGIN){
                        val sharedPref = activity?.getSharedPreferences("shared_preferences", 0)
                        val editor = sharedPref?.edit()
                        editor?.putString("token", response)
                        editor?.apply()

                        //Navigate to the bookings fragment
                        callback?.onLoginSuccess()

                    }else{
                        Toast.makeText(context, response, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }

        return view
    }

    companion object {

        private const val WRONG_LOGIN = "Wrong..."

        @JvmStatic
        fun newInstance() = FragmentLogin()
    }
}