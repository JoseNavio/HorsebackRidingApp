package com.navio.horsebackridingapp.data

data class BookingRequest(
    val user_id: Int,
    val horse_id: Int,
    val date: String,
    val hour: Int,
    val comment: String
)