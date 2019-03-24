package no.vipps.md.tictactoev2.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_startup.*

import no.vipps.md.tictactoev2.R

class StartupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_startup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        singlePlayerBtn.setOnClickListener {
            displayRegisterFragment(true)
        }

        twoPlayerBtn.setOnClickListener {
            displayRegisterFragment(false)
        }
    }

    fun displayRegisterFragment(botCheckedState: Boolean) {
        val registerFragment = RegisterFragment.newInstance(botCheckedState)
        val fragmentTransaction = fragmentManager
            ?.beginTransaction()

        fragmentTransaction
            ?.replace(R.id.fragment_container, registerFragment)
            ?.addToBackStack(null)
            ?.commit()

    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            StartupFragment().apply {

            }
    }
}
