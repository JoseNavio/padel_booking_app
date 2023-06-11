package me.josena.padel.adapter

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import me.josena.padel.data.Booking
import me.josena.padel.databinding.ItemBookingBinding

class ViewHolderBookings(itemView: View, adapterBookings: AdapterBookings) :
    RecyclerView.ViewHolder(itemView) {

    private val bindingBooking = ItemBookingBinding.bind(itemView)

    init {

        itemView.setOnClickListener {
            adapterBookings.currentSelected(adapterPosition)
        }
    }

    fun render(booking: Booking) {

        bindingBooking.cardView.setCardBackgroundColor(Color.WHITE)

        bindingBooking.labelName.text = booking.name
        bindingBooking.labelTelephone.text = booking.telephone
        bindingBooking.labelDate.text = booking.date
        bindingBooking.labelTime.text = booking.hour
        bindingBooking.labelComment.text = booking.comment
    }

    fun renderSelected(booking: Booking) {

        bindingBooking.cardView.setCardBackgroundColor(Color.GRAY)

        bindingBooking.labelName.text = booking.name
        bindingBooking.labelTelephone.text = booking.telephone
        bindingBooking.labelDate.text = booking.date
        bindingBooking.labelTime.text = booking.hour
        bindingBooking.labelComment.text = booking.comment
    }
}