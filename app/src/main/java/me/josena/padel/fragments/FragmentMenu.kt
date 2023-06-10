package me.josena.padel.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import me.josena.padel.OnFragmentChosen
import me.josena.padel.R
import me.josena.padel.databinding.FragmentMenuLayoutBinding

class FragmentMenu(private val listener: OnFragmentChosen) : Fragment() {

    private lateinit var binding: FragmentMenuLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMenuLayoutBinding.inflate(layoutInflater)

        setButtons()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Hide top bar or modify it --> Not visible
        (activity as AppCompatActivity).supportActionBar?.apply {
//            actionBar.hide()
            title = getString(R.string.app_title)
            subtitle = getString(R.string.fragment_menu_subtitle)
        }
        return binding.root
    }

    //Sets buttons to move between fragments
    private fun setButtons() {

        binding.buttonList.setOnClickListener {
            listener.onListChosen()
        }
        binding.buttonBooking.setOnClickListener {
            listener.onBookingChosen()
        }
    }

    companion object {
        fun newInstance(listener: OnFragmentChosen) = FragmentMenu(listener)
    }
}