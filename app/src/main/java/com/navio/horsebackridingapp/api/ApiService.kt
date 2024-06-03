package com.navio.horsebackridingapp.api

import com.navio.horsebackridingapp.data.LoginRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun userLogin(@Body request: LoginRequest): Call<ResponseBody>
}