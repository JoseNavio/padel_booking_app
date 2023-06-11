package me.josena.padel.utils

import android.annotation.SuppressLint
import android.os.Parcel
import com.google.android.material.datepicker.CalendarConstraints
import java.util.*

@SuppressLint("ParcelCreator")
class CustomDateValidator(private val currentYear: Int, private val currentMonth: Int) :
    CalendarConstraints.DateValidator {

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun isValid(date: Long): Boolean {
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentCalendar = Calendar.getInstance()
        currentCalendar.set(currentYear, currentMonth, currentDay)

        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.timeInMillis = date

        return (selectedCalendar.after(currentCalendar) && selectedCalendar.get(Calendar.YEAR) == currentYear) ||
                selectedCalendar.get(Calendar.YEAR) > currentYear
    }
}