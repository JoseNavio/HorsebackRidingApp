package com.navio.horsebackridingapp.dialogs

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.navio.horsebackridingapp.data.Booking
import com.navio.horsebackridingapp.data.BookingUpdate
import com.navio.horsebackridingapp.data.Horse
import com.navio.horsebackridingapp.databinding.DialogEditBookingBinding
import com.navio.horsebackridingapp.fragments.bookings.FragmentBookings

class DialogEditBooking(
    private val context: Context,
    private val horses: List<Horse>,
    private val callback: FragmentBookings.OnItemEdited?
) {

    private val binding = DialogEditBookingBinding.inflate(LayoutInflater.from(context))
    private var dialog: AlertDialog? = null

    fun create(booking: Booking) {
        dialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .create()

        // Get the Spinner from the dialog
        val spinner: Spinner = binding.dialogEditBookingFieldHorse
        // Create an ArrayAdapter using a default spinner layout and the array of horses
        val adapter = ArrayAdapter(context, R.layout.simple_spinner_item, horses.map { it.name })
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinner.adapter = adapter
        // Set the selected item to the horse of the booking
        val selectedHorse = horses.find { it.name == booking.horseName }
        if (selectedHorse != null) {
            spinner.setSelection(horses.indexOf(selectedHorse))
        }

        binding.dialogEditBookingFieldDate.setText(booking.date)
        binding.dialogEditBookingFieldHour.setText(booking.hour)
        binding.dialogEditBookingFieldComment.setText(booking.comment)

        //BUTTONS
        //Cancel
        binding.dialogEditBookingButtonCancel.setOnClickListener {
            dismiss()
        }
        //Add
        binding.dialogEditBookingButtonSave.setOnClickListener {

            val horseId = horses[spinner.selectedItemPosition].id
            val date = binding.dialogEditBookingFieldDate.text.toString()
            val hour = binding.dialogEditBookingFieldHour.text.toString()
            val comment = binding.dialogEditBookingFieldComment.text.toString()

            val editedBooking = BookingUpdate(
                booking.id,
                horseId,
                date,
                hour,
                comment
            )

            callback?.onItemEdited(editedBooking)
            dismiss()
        }

    }

    fun show() {
        dialog?.show()
    }

    private fun dismiss() {
        dialog?.dismiss()
    }

}