package me.josena.padel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import me.josena.padel.databinding.ActivityMainBinding
import me.josena.padel.fragments.FragmentBooking
import me.josena.padel.fragments.FragmentList
import me.josena.padel.fragments.FragmentMenu

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Attach initial menu fragment
        attachMenuFragment()
    }
    //Replace fragments
    //Launches menu fragment with a callback to listen which button has been click on it
    private fun attachMenuFragment() {

        supportFragmentManager.commit {
            setReorderingAllowed(true)//Let commit operations decide better operation's order
            replace(
                binding.fragmentContainerActivity.id,
                FragmentMenu.newInstance(object : OnFragmentChosen {
                    //Replace the menu fragment for the list one
                    override fun onListChosen() {
                        attachListFragment()
                    }
                    //Replace the menu fragment for the booking one
                    override fun onBookingChosen() {
                        attachBookingFragment()
                    }
                })
            )
        }
    }

    //Launches fragments
    private fun attachListFragment() {

        supportFragmentManager.commit {
            setReorderingAllowed(true)//Let commit operations decide better operation's order
            replace(binding.fragmentContainerActivity.id, FragmentList.newInstance())
            addToBackStack(null)
        }
    }

    //Launches location fragment
    private fun attachBookingFragment() {

        supportFragmentManager.commit {
            setReorderingAllowed(true)//Let commit operations decide better operation's order
            replace(binding.fragmentContainerActivity.id, FragmentBooking.newInstance())
            addToBackStack(null)
        }
    }
}

interface OnFragmentChosen {
    fun onBookingChosen()
    fun onListChosen()
}
