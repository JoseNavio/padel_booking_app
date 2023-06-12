package me.josena.padel.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.josena.padel.OnBookingAdded
import me.josena.padel.R
import me.josena.padel.adapter.AdapterBookings
import me.josena.padel.data.Booking
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

    //Add booking
    fun addBooking(booking: Booking) {
        adapterBookings.addBooking(booking)
    }

    //Deletes a booking
    private fun deleteSelected() {
        adapterBookings.deleteCurrentSelected()
    }

    //Edits a booking
    private fun modifySelected() {

    }

    //Find a booking by name or date
    private fun findBookings() {

    }

    //Launches a dialog to modify booking
    private fun showModifyDialog() {

//        val dialogFileBinding: FragmentBookingLayoutBinding = FragmentBookingLayoutBinding.inflate(layoutInflater)
//
//        val alertDialogFile = context?.let {
//            AlertDialog.Builder(it)
//                .setTitle("GUARDAR IMAGEN")
//                .setView(dialogFileBinding.root)
//                .setPositiveButton("Guardar") { _, _ ->
//                    fileName = dialogFileBinding.fieldFileName.text.toString()
//                    callback.onTextPassed(fileName)
//                }
//                .setNegativeButton("Cancelar", null)
//                .create()
//        }
//        alertDialogFile?.show()
    }

    companion object {
        fun newInstance() = FragmentList()
    }
}