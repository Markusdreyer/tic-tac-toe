package no.vipps.md.tictactoev2.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.fragment_register.*
import no.vipps.md.tictactoev2.DatabaseHandler
import no.vipps.md.tictactoev2.R

class RegisterFragment : Fragment(), View.OnClickListener {
    private var botChecked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }


    //TODO: Name required
    //TODO: Cannot have same name
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        player1Input.visibility = View.GONE

        newPlayer1Btn.setOnClickListener{
            newButtonHandler(player1Input, newPlayer1Btn, existingPlayer1Btn)
        }

        newPlayer2Btn.setOnClickListener{
            newButtonHandler(player2Input, newPlayer2Btn, existingPlayer2Btn)
        }

        existingPlayer1Btn.setOnClickListener {
            existingButtonHandler(newPlayer1Btn, existingPlayer1Btn, player1Input, player1Change)
        }

        existingPlayer2Btn.setOnClickListener{
            existingButtonHandler(newPlayer2Btn, existingPlayer2Btn, player2Input, player2Change)
        }

        player1Change.setOnClickListener{
            playerSpinner.performClick()
        }

        player2Change.setOnClickListener{
            playerSpinner.performClick()
        }

        botChecked = arguments!!.getBoolean("botCheckedState", false)

        if(botChecked) {
            player2Input!!.setText("TTTBOT")
            player2Input.isEnabled = false
            newPlayer2Btn.visibility = View.INVISIBLE
            existingPlayer2Btn.visibility = View.INVISIBLE
        } else {
            player2Input.visibility = View.GONE
        }

        nextBtn.setOnClickListener(this)
    }

    private fun newButtonHandler(playerInput: TextView, newPlayerBtn: Button, existingPlayerBtn: Button) {
        playerInput.visibility = View.VISIBLE
        newPlayerBtn.visibility = View.INVISIBLE
        existingPlayerBtn.visibility = View.INVISIBLE
    }

    private fun existingButtonHandler(newPlayerBtn: Button, existingPlayerBtn: Button, playerInput: TextView, changeBtn: Button) {
        newPlayerBtn.visibility = View.INVISIBLE
        existingPlayerBtn.visibility = View.INVISIBLE
        prepareSpinnerLogic(playerInput, changeBtn)
        playerSpinner.performClick()
    }

    fun prepareSpinnerLogic(inputField: TextView, changeBtn: Button) {
        populateSpinner()
        playerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                inputField.text = playerSpinner.selectedItem.toString()
                inputField.visibility = View.VISIBLE
                changeBtn.visibility = View.VISIBLE
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                player1Change.visibility = View.VISIBLE
            }
        }
    }


    fun populateSpinner() {
        val db = DatabaseHandler(activity!!.applicationContext)
        val data = db.readData()
        var playerList = mutableListOf<String>()

        for(i in 0..(data.size -1)) {
            if(data[i].name != "TTTBOT") {
                playerList.add(data[i].name)
            }
        }

        if(playerList.contains(player1Input.text.toString())) {
            playerList.remove(player1Input.text.toString())
        }

        val adapter = ArrayAdapter(activity,
            android.R.layout.simple_spinner_item,
            playerList)

        playerSpinner.adapter = adapter

    }

    override fun onClick(view: View?) {
        var player1 = player1Input.text.toString()
        var player2 = player2Input.text.toString()
        if(player1.isNotEmpty() && player2.isNotEmpty() && player1 != player2) {
            val gameFragment = GameFragment.newInstance(player1, player2)
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager
                ?.beginTransaction()
            fragmentTransaction
                ?.replace(R.id.fragment_container, gameFragment)
                ?.addToBackStack(null)
                ?.commit()
        } else if(player2 == player1){
            Toast.makeText(activity, "Player name cannot be the same", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "Player names cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(botCheckedState: Boolean): RegisterFragment {
            val registerFragment = RegisterFragment()

            val args = Bundle()
            args.putBoolean("botCheckedState", botCheckedState)
            registerFragment.arguments = args

            return registerFragment
        }
    }
}
