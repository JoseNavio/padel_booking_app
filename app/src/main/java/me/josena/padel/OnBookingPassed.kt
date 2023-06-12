package me.josena.padel

import me.josena.padel.data.Booking

interface OnBookingPassed {

    fun onBookingPassed(booking: Booking)
}