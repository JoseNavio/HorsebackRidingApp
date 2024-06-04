package com.navio.horsebackridingapp.data

import com.google.gson.annotations.SerializedName

data class Booking(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("horse_id")
    val horseId: Int,
    var horseName: String,
    val date: String,
    val hour: String,
    val comment: String
)
