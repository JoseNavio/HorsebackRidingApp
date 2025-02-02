package com.navio.horsebackridingapp.fragments.bookings

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.navio.horsebackridingapp.R
import com.navio.horsebackridingapp.data.Booking

class AdapterBookings(val listener: FragmentBookings.OnItemChanges) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var bookings: MutableList<Booking> = mutableListOf()
    private var selectedBooking: Int = RecyclerView.NO_POSITION
    private var lastSelectedBooking: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (bookings.isEmpty()) {
            Log.d("Navio_log", "No bookings found")
        }

        bookings.forEach {
            Log.d("Navio_log", "Booking: $it")
        }

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)

        return ViewHolderBookings(itemView, this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHolderBookings) {
            if (position == selectedBooking) {
                holder.renderSelected(bookings[position])
            } else {
                holder.render(bookings[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return bookings.size
    }

    fun selectedBooking(callback: OnItemSelected) {
        //Return the selected position
        if (selectedBooking != RecyclerView.NO_POSITION) {
            callback.onItemSelected(selectedBooking)
            selectedBooking = RecyclerView.NO_POSITION
        }
    }

    fun updateBookings(updatedBookings: MutableList<Booking>) {
        bookings = updatedBookings
        notifyDataSetChanged()
    }

    fun editBooking(booking: Booking) {
        listener.onItemEdited(booking)
    }

    fun deleteBooking(bookingId: Int) {

        bookings.removeIf {
            it.id == bookingId
        }
        listener.onItemDeleted(bookingId)

        notifyDataSetChanged()
    }

    fun currentSelected(position: Int) {
        selectedBooking = if (selectedBooking == position) {
            RecyclerView.NO_POSITION // Deselect if already selected
        } else {
            lastSelectedBooking = selectedBooking
            position // Select the newly clicked item
        }
        notifyItemChanged(lastSelectedBooking) // Refresh the adapter
        notifyItemChanged(selectedBooking) // Refresh the adapter
    }
}

interface OnItemSelected {
    fun onItemSelected(position: Int) {}
}