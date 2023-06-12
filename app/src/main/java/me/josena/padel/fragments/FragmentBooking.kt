package me.josena.padel.fragments

import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.josena.padel.OnBookingPassed
import me.josena.padel.R
import me.josena.padel.data.Booking
import me.josena.padel.databinding.FragmentBookingLayoutBinding
import me.josena.padel.utils.CustomDateValidator
import java.text.SimpleDateFormat
import java.util.*

class FragmentBooking(val bookingListener: OnBookingPassed) : Fragment() {

    private lateinit var binding: FragmentBookingLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBookingLayoutBinding.inflate(layoutInflater)

        setButtons()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Hide top bar or modify it --> Not visible
        (activity as AppCompatActivity).supportActionBar?.apply {
            //hide()
            title = getString(R.string.app_title)
            subtitle = getString(R.string.fragment_booking_subtitle)
        }
        return binding.root
    }

    //Sets buttons to move between fragments
    private fun setButtons() {

        //Confirm
        binding.buttonConfirm.setOnClickListener {
            confirmBooking()
        }

        //DatePicker
        binding.fieldDate.setOnClickListener {
            showMaterialCalendarDialog()
        }
        //Time clock
        binding.fieldTime.setOnClickListener {
            showTimePickerDialog()
        }

        binding.buttonBack.setOnClickListener {
            //Same than press back button
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    //Picks date
    private fun showMaterialCalendarDialog() {

        val builderMaterialDatePicker = MaterialDatePicker.Builder.datePicker()
        val customMaterialStyle = R.style.MaterialCalendarThemeBackground
        builderMaterialDatePicker.setTheme(customMaterialStyle)
            .setTitleText("Fecha del Partido")

        //Get current date
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)

        //You can instead set de start and the end of the constraints
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(CustomDateValidator(currentYear, currentMonth))

        builderMaterialDatePicker.setCalendarConstraints(constraintsBuilder.build())
        val materialDatePicker = builderMaterialDatePicker.build()

        //When clicking a date
        materialDatePicker.addOnPositiveButtonClickListener { selection ->

            val calendar = Calendar.getInstance()
            //Transform selected date to a calendar object
            calendar.timeInMillis = selection
            val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormatter.format(calendar.time)
            binding.fieldDate.setText(formattedDate)
        }
        childFragmentManager.let { it -> materialDatePicker.show(it, "CustomDatePicker") }
    }

    //Picks hour
    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = 0

        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                if (hourOfDay in 9..23 && minute == 0) {
                    val selectedTime = String.format("%02d:00", hourOfDay)
                    binding.fieldTime.setText(selectedTime)
                } else {
                    // Here, we simply show a toast message
                    Toast.makeText(
                        context,
                        "Selecciona una hora entre las 9 y las 23",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun confirmBooking() {
        val name = binding.fieldName.text.toString().trim()
        val telephone = binding.fieldTelephone.text.toString().trim()
        val date = binding.fieldDate.text.toString().trim()
        val hour = binding.fieldTime.text.toString().trim()
        val comment = binding.fieldComment.text.toString().trim()

        if (name.isNotEmpty() && telephone.isNotEmpty() && date.isNotEmpty() && hour.isNotEmpty()) {

            val newBooking = Booking(name, telephone, date, hour, comment)
//            //Insert using Room
//            lifecycleScope.launch(Dispatchers.IO) { }

            //todo Mocked insert
            bookingListener.onBookingPassed(newBooking)

            //Notify
            sendWhatsApp(newBooking.toString())
            resetFields()

        } else {
            // Display an error message or handle the case when any field is blank
            Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetFields() {
        binding.fieldName.setText("")
        binding.fieldTelephone.setText("")
        binding.fieldDate.setText("")
        binding.fieldTime.setText("")
        binding.fieldComment.setText("")
    }

    //WhatsApp
    //It is necessary to add the package query on the manifest in order to use this...
    private fun isWhatsAppInstalled(): Boolean {
        val whatsAppPackageName = "com.whatsapp"
        return try {
            val packageInfo = context?.packageManager?.getPackageInfoCompat(
                whatsAppPackageName,
                PackageManager.GET_ACTIVITIES
            )
            packageInfo != null
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun PackageManager.getPackageInfoCompat(
        packageName: String,
        flags: Int
    ): PackageInfo =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
        } else {
            @Suppress("DEPRECATION") getPackageInfo(packageName, flags)
        }

    private fun sendWhatsApp(message: String) {

        isWhatsAppInstalled()

        if (isWhatsAppInstalled()) {
            val number = binding.fieldTelephone?.text.toString()
            if (message.isNotBlank() && number.isNotBlank()) {

                val whatsIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://api.whatsapp.com/send?phone=34$number&text=$message")
                )
                startActivity(whatsIntent)
            } else {
                Toast.makeText(
                    context,
                    "Los campos no pueden estar vacios.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } else {
            Toast.makeText(
                context,
                "No existe la aplicación de WhatsApp en el dispositivo para mandar la notificación.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        fun newInstance(bookingListener: OnBookingPassed) = FragmentBooking(bookingListener)
    }
}