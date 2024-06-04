package com.navio.horsebackridingapp.fragments.bookings

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.navio.horsebackridingapp.data.Booking
import com.navio.horsebackridingapp.databinding.ItemBookingBinding

class ViewHolderBookings(itemView: View, private val adapterBookings: AdapterBookings) : RecyclerView.ViewHolder(itemView) {

    private val bindingBooking = ItemBookingBinding.bind(itemView)

    init{
        itemView.setOnClickListener{
            adapterBookings.currentSelected(adapterPosition)
        }
    }
    fun render(booking: Booking) {
        bindingBooking.cardView.setCardBackgroundColor(Color.WHITE)
        bindingBooking.itemBookingHorseName.text = booking.horseName
        bindingBooking.itemBookingDate.text = booking.date
        bindingBooking.itemBookingHour.text = booking.hour
        bindingBooking.itemBookingComment.text = booking.comment

        bindingBooking.itemBookingButtonDelete.setOnClickListener {
            adapterBookings.deleteBooking(booking.id)
        }
    }
    fun renderSelected(booking: Booking){

        bindingBooking.cardView.setCardBackgroundColor(Color.GRAY)
        bindingBooking.itemBookingHorseName.text = booking.horseName
        bindingBooking.itemBookingDate.text = booking.date
        bindingBooking.itemBookingHour.text = booking.hour
        bindingBooking.itemBookingComment.text = booking.comment
    }
}