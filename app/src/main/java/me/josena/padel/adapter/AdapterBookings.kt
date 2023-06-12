package me.josena.padel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.josena.padel.R
import me.josena.padel.data.Booking

class AdapterBookings(private var bookings: MutableList<Booking>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedBooking: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

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

    fun updateBookings(updatedBookings: MutableList<Booking>) {
        bookings = updatedBookings
        notifyDataSetChanged()
    }

    fun currentSelected(position: Int) {

        val lastSelectedPosition = selectedBooking

        selectedBooking = if (selectedBooking == position) {
            RecyclerView.NO_POSITION // Deselect if already selected
        } else {
            position // Select the newly clicked item
        }

        notifyItemChanged(lastSelectedPosition) // Refresh the adapter for the previously selected item
        notifyItemChanged(selectedBooking) // Refresh the adapter for the currently selected item
    }
    //Search
    fun searchByName(){

    }
    fun searchByDate(){

    }
    //Add
    fun addBooking(booking: Booking){
        bookings.add(booking)
        notifyItemChanged(bookings.lastIndex)
    }
    //Delete
    fun deleteCurrentSelected() {

        if (selectedBooking != RecyclerView.NO_POSITION) {
            bookings.removeAt(selectedBooking)
            notifyItemRemoved(selectedBooking)
            selectedBooking = RecyclerView.NO_POSITION
        }
    }
    //Modify
    fun modifyCurrentSelected() {

        if (selectedBooking != RecyclerView.NO_POSITION) {
            bookings.set(selectedBooking, Booking("","", "", "", ""))
            notifyItemChanged(selectedBooking)
            selectedBooking = RecyclerView.NO_POSITION
        }
    }
}
