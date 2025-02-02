package com.navio.horsebackridingapp.api

import com.navio.horsebackridingapp.data.BookingRequest
import com.navio.horsebackridingapp.data.BookingUpdate
import com.navio.horsebackridingapp.data.LoginRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("login")
    fun userLogin(@Body request: LoginRequest): Call<ResponseBody>

    @GET("get-all-horses")
    suspend fun getAllHorses(): Response<ResponseBody>

    @GET("get-all-bookings")
    suspend fun getAllBookings(): Response<ResponseBody>

    @POST("create-booking")
    suspend fun createBookingAPI(@Body request: BookingRequest): Response<ResponseBody>

    //Update booking
    @PUT("update-booking/{booking}")
    suspend fun updateBookingAPI(@Path("booking") bookingId: Int, @Body request: BookingUpdate): Response<ResponseBody>

    @DELETE("delete-booking/{booking}")
    suspend fun deleteBookingAPI(@Path("booking") bookingId: Int): Response<ResponseBody>
}