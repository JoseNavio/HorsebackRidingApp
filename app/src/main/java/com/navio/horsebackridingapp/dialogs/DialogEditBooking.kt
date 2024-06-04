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

        val spinner: Spinner = binding.dialogEditBookingFieldHorse
        val spinnerHorses = horses.filter { horse -> horse.isSick == 0 }
        val adapter = ArrayAdapter(context, R.layout.simple_spinner_item, spinnerHorses.map { it.name })

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val selectedHorse = spinnerHorses.find { it.name == booking.horseName }
        if (selectedHorse != null) {
            spinner.setSelection(spinnerHorses.indexOf(selectedHorse))
        }

        binding.dialogEditBookingFieldDate.setText(booking.date)
        binding.dialogEditBookingFieldHour.setText(booking.hour)
        binding.dialogEditBookingFieldComment.setText(booking.comment)

        binding.dialogEditBookingButtonCancel.setOnClickListener {
            dismiss()
        }
        binding.dialogEditBookingButtonSave.setOnClickListener {
            val horseId = spinnerHorses[spinner.selectedItemPosition].id
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