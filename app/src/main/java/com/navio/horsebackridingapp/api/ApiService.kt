package com.navio.horsebackridingapp.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("loginusername") username: String,
        @Field("loginpassword") password: String
    ): Call<ResponseBody>
}