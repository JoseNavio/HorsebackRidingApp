package com.navio.horsebackridingapp.data

data class BookingRequest(
    val horse_id: Int,
    val date: String,
    val hour: Int,
    val comment: String
)