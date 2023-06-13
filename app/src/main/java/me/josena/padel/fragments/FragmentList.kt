package me.josena.padel.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.josena.padel.R
import me.josena.padel.adapter.AdapterBookings
import me.josena.padel.data.Booking
import me.josena.padel.databinding.FragmentBookingLayoutBinding
import me.josena.padel.databinding.FragmentListLayoutBinding
import me.josena.padel.utils.Utils

class FragmentList() : Fragment() {

    private lateinit var binding: FragmentListLayoutBinding

    private lateinit var recycler: RecyclerView
    private lateinit var adapterBookings: AdapterBookings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentListLayoutBinding.inflate(layoutInflater)

        setButtons()
        initAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Hide top bar or modify it --> Not visible
        (activity as AppCompatActivity).supportActionBar?.apply {
//            actionBar.hide()
            title = getString(R.string.app_title)
            subtitle = getString(R.string.fragment_list_subtitle)
        }
        return binding.root
    }

    //Sets buttons to move between fragments
    private fun setButtons() {

        binding.buttonSearch.setOnClickListener { findBookings() }
        binding.buttonModify.setOnClickListener { modifySelected() }
        binding.buttonDelete.setOnClickListener { deleteSelected() }
        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initAdapter() {

        lifecycleScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                recycler = binding.recyclerBookings
                adapterBookings = AdapterBookings(Utils.MOCKED_LIST)
                recycler.adapter = adapterBookings
                recycler.layoutManager = LinearLayoutManager(context)
            }
        }
    }

    //Deletes a booking
    private fun deleteSelected() {
        adapterBookings.deleteCurrentSelected()
    }

    //Edits a booking
    private fun modifySelected() {
        showModifyDialog()
    }

    //Find a booking by name or date
    private fun findBookings() {
        showSearchDialog()
    }

    //Launches a dialog to modify booking
    private fun showModifyDialog() {

        val dialogFileBinding: FragmentBookingLayoutBinding =
            FragmentBookingLayoutBinding.inflate(layoutInflater)
        val selectedPosition = adapterBookings.getCurrentSelected()

        if (selectedPosition != RecyclerView.NO_POSITION) {

            val selectedBooking = Utils.MOCKED_LIST[selectedPosition]

            //Initialize views
            dialogFileBinding.fieldName.setText(selectedBooking.name)
            dialogFileBinding.fieldTelephone.setText(selectedBooking.telephone)
            dialogFileBinding.fieldDate.setText(selectedBooking.date)
            dialogFileBinding.fieldDate.isClickable = true
            dialogFileBinding.fieldDate.isFocusable = true
            dialogFileBinding.fieldDate.isFocusableInTouchMode = true
            dialogFileBinding.fieldDate.isCursorVisible = true
            dialogFileBinding.fieldTime.setText(selectedBooking.hour)
            dialogFileBinding.fieldTime.isClickable = true
            dialogFileBinding.fieldTime.isFocusable = true
            dialogFileBinding.fieldTime.isFocusableInTouchMode = true
            dialogFileBinding.fieldTime.isCursorVisible = true
            dialogFileBinding.fieldComment.setText(selectedBooking.comment)
            //Not needed here
            dialogFileBinding.buttonConfirm.visibility = GONE
            dialogFileBinding.buttonBack.visibility = GONE

            val alertDialogFile = context?.let {
                AlertDialog.Builder(it)
                    .setTitle("Modificar reserva")
                    .setView(dialogFileBinding.root)
                    .setPositiveButton("Guardar") { _, _ ->

                        val newName = dialogFileBinding.fieldName.text.toString()
                        val newTelephone = dialogFileBinding.fieldTelephone.text.toString()
                        val newDate = dialogFileBinding.fieldDate.text.toString()
                        val newTime = dialogFileBinding.fieldTime.text.toString()
                        val newComment = dialogFileBinding.fieldComment.text.toString()

                        var newBooking =
                            Booking(newName, newTelephone, newDate, newTime, newComment)
                        Utils.MOCKED_LIST[selectedPosition] = newBooking

                        adapterBookings.updateBookings(Utils.MOCKED_LIST)
                    }
                    .setNegativeButton("Cancelar", null)
                    .create()
            }
            alertDialogFile?.show()
        }
    }

    private fun showSearchDialog() {

        val dialogSearchBinding: FragmentBookingLayoutBinding =
            FragmentBookingLayoutBinding.inflate(layoutInflater)
        val selectedPosition = adapterBookings.getCurrentSelected()

        //Initialize views
        dialogSearchBinding.fieldDate.isClickable = true
        dialogSearchBinding.fieldDate.isFocusable = true
        dialogSearchBinding.fieldDate.isFocusableInTouchMode = true
        dialogSearchBinding.fieldDate.isCursorVisible = true
        //Not needed here
        dialogSearchBinding.buttonConfirm.visibility = GONE
        dialogSearchBinding.buttonBack.visibility = GONE
        dialogSearchBinding.linearBookingTime.visibility = GONE
        dialogSearchBinding.linearComment.visibility = GONE
        dialogSearchBinding.linearBookingTelephone.visibility = GONE

        val tempList = mutableListOf<Booking>()

        val alertDialogFile = context?.let {
            AlertDialog.Builder(it)
                .setTitle("Buscar reserva")
                .setView(dialogSearchBinding.root)
                .setPositiveButton("Buscar") { _, _ ->

                    if (dialogSearchBinding.fieldName.text.isNotBlank()) {

                        for (booking in Utils.MOCKED_LIST) {

                            if (booking.name.trim() == dialogSearchBinding.fieldName.text.toString()
                                    .trim()
                            ){
                                tempList.add(booking)
                            }
                        }
                    }

                    if (dialogSearchBinding.fieldDate.text.isNotBlank()) {

                        for (booking in Utils.MOCKED_LIST) {

                            if (booking.date.trim() == dialogSearchBinding.fieldDate.text.toString()
                                    .trim()
                            ){
                                tempList.add(booking)
                            }
                        }
                    }
                    binding.buttonModify.visibility = GONE
                    binding.buttonDelete.visibility = GONE
                    adapterBookings.updateBookings(tempList)
                }
                .setNegativeButton("Cancelar", null)
                .create()
        }
        alertDialogFile?.show()
    }

    companion object {
        fun newInstance() = FragmentList()
    }
}