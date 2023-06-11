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
import me.josena.padel.R
import me.josena.padel.adapter.AdapterBookings
import me.josena.padel.data.Booking
import me.josena.padel.databinding.FragmentListLayoutBinding
import me.josena.padel.databinding.FragmentMenuLayoutBinding

class FragmentList : Fragment() {

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

        binding.buttonSearch.setOnClickListener {  }
        binding.buttonModify.setOnClickListener {  }
        binding.buttonDelete.setOnClickListener {  }
        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initAdapter() {

        val bookings = mutableListOf<Booking>(
            Booking("Amador Rivas","666446644","17/03/2021","11:00","No"),
            Booking("Amador Rivas","666446644","17/03/2021","11:00","No"),
            Booking("Amador Rivas","666446644","17/03/2021","11:00","No"),
            Booking("Amador Rivas","666446644","17/03/2021","11:00","No"),
            Booking("Amador Rivas","666446644","17/03/2021","11:00","No"),
            Booking("Amador Rivas","666446644","17/03/2021","11:00","No"),
            Booking("Amador Rivas","666446644","17/03/2021","11:00","No"),
            Booking("Javier Maroto","777555333","23/05/2022","17:00","Huevon, huevon, huevon...")
        )

        lifecycleScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                recycler = binding.recyclerBookings
                adapterBookings = AdapterBookings(bookings)
                recycler.adapter = adapterBookings
                recycler.layoutManager = LinearLayoutManager(context)
            }
        }
    }


    companion object {
        fun newInstance() = FragmentList()
    }
}