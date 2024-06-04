package com.navio.horsebackridingapp.data

import com.google.gson.annotations.SerializedName

data class Horse(

    val id: Int,
    @SerializedName("horse_name")
    val name: String,
    val breed: String,
    val gender: String,
    val age: Int,
    @SerializedName("is_sick")
    val isSick: Int,
    val observations: String
){
    fun getIsSick(): Boolean {
        return isSick == 1
    }
}
