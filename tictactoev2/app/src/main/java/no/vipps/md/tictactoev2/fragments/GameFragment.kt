package no.vipps.md.tictactoev2.fragments

import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_game.*
import no.vipps.md.tictactoev2.DatabaseHandler
import no.vipps.md.tictactoev2.Player
import no.vipps.md.tictactoev2.R

class GameFragment : Fragment(), View.OnClickListener {
    private var oArray: MutableList<String> = mutableListOf()
    private var xArray: MutableList<String> = mutableListOf()
    private val winningPatterns: Array<String> = arrayOf("012", "345", "678", "258", "048", "642", "147", "036") //Problemer med leading 0
    private var activePlayer: String = ""
    private val playerNames: Array<String> = arrayOf("", "")
    private var occupiedSelections: MutableList<Int> = mutableListOf()
    private var availableSelections: MutableList<Int> = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7 ,8)
    private var gameInProgress = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnArray: Array<Button> = arrayOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn6, btn7, btn8) //TODO: Refactor onclick for btns using this array
        val timer: Long = 0
        chronometer.base=SystemClock.elapsedRealtime()+timer
        chronometer.start()

        leaderboardText.setOnClickListener{
            val leaderboardFragment = LeaderboardFragment.newInstance(1)
            val fragmentManager = fragmentManager
            leaderboardFragment.show(fragmentManager, "")
        }

        this.playerNames[0] = arguments!!.getString("player1","")
        this.playerNames[1] = arguments!!.getString("player2","")

        activePlayer = playerNames[0]
        activePlayerName.text = "$activePlayer's turn"

        btn0.setOnClickListener(this)
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)
        btn5.setOnClickListener(this)
        btn6.setOnClickListener(this)
        btn7.setOnClickListener(this)
        btn8.setOnClickListener(this)



        resetBtn.setOnClickListener{
            for(btn in btnArray) {
                btn.text = ""
            }
            chronometer.base=SystemClock.elapsedRealtime()+timer
            chronometer.start()
            gameInProgress = true
            activePlayer = playerNames[0]
            activePlayerName.text = "$activePlayer's turn"
            xArray.clear()
            oArray.clear()
            occupiedSelections.clear()
            availableSelections = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7 ,8)
        }
    }



    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn0 -> registerMove(0, btn0)
            R.id.btn1 -> registerMove(1, btn1)
            R.id.btn2 -> registerMove(2, btn2)
            R.id.btn3 -> registerMove(3, btn3)
            R.id.btn4 -> registerMove(4, btn4)
            R.id.btn5 -> registerMove(5, btn5)
            R.id.btn6 -> registerMove(6, btn6)
            R.id.btn7 -> registerMove(7, btn7)
            R.id.btn8 -> registerMove(8, btn8)
        }
    }

    private fun registerMove(position: Int, button: Button) {
        if (gameInProgress && !occupiedSelections.contains(position)) {
            occupiedSelections.add(position)
            if (activePlayer == playerNames[0]) {
                button.text = "O"
                oArray.add(position.toString())
                checkForWinner(oArray)
                activePlayer = playerNames[1]
            } else {
                button.text = "X"
                xArray.add(position.toString())
                checkForWinner(xArray)
                activePlayer = playerNames[0]
            }

            if(gameInProgress) {
                activePlayerName.text = "$activePlayer's turn"
                updateAvailableSelections()

                if(availableSelections.isEmpty()) {
                    gameInProgress = false
                    chronometer.stop()
                    activePlayerName.text = "It's a draw!"
                }

                if(activePlayer == "TTTBOT") {
                    performBotMove(calculateBotMoves())
                }
            }
        }
    }

    private fun performBotMove(position: Int){
        when (position) {
            0 -> registerMove(0, btn0)
            1 -> registerMove(1, btn1)
            2 -> registerMove(2, btn2)
            3 -> registerMove(3, btn3)
            4 -> registerMove(4, btn4)
            5 -> registerMove(5, btn5)
            6 -> registerMove(6, btn6)
            7 -> registerMove(7, btn7)
            8 -> registerMove(8, btn8)
        }
    }

    private fun updateAvailableSelections() {
        for (i in occupiedSelections) {
            if (availableSelections.contains(i)) {
                availableSelections.removeAt(availableSelections.indexOf(i))
            }
        }
    }

    private fun calculateBotMoves(): Int {
        val center = 4
        val corners = arrayOf(0, 2, 6, 8)
        val inbetweeners = arrayOf(1, 3, 5, 7)
        var bestMove = 0
        var movesOnLine = 0


        for(pattern in winningPatterns) {
            for (move in oArray) {
                if(pattern.contains(move)) {
                    movesOnLine++
                }
            }
            if(movesOnLine == 2) {
                for(i in 0..8) {
                    if (pattern.contains(i.toString()) && !occupiedSelections.contains(i)) {
                        return i
                    }
                }
            }


            movesOnLine = 0
        }

        if(availableSelections.contains(center)) {
            return center
        } else {
            for (corner in corners) {
                if(availableSelections.contains(corner)) {
                    return corner
                }
            }
            for (inbetweener in inbetweeners) {
                if(availableSelections.contains(inbetweener)) {
                    return inbetweener
                }
            }
        }
        return bestMove
    }

    private fun checkForWinner(playerMoves: MutableList<String>) {
        var movesOnLine = 0
        for(pattern in winningPatterns) {
            for (move in playerMoves) {
                if(pattern.contains(move)) {
                    movesOnLine++
                }
            }
            if(movesOnLine == 3) {
                gameInProgress = false
                chronometer.stop()
                registerWinningPlayer()
            }
            movesOnLine = 0
        }
    }

    private fun registerWinningPlayer() {
        val db = DatabaseHandler(activity!!.applicationContext)

        if(db.checkIfPlayerExists(activePlayer)) {
            db.incrementScore(activePlayer)
        } else {
            var user = Player(activePlayer, 1)
            db.insertData(user)
        }

        activePlayerName.text = "$activePlayer won!"
    }

    companion object {
        @JvmStatic
        fun newInstance(player1: String, player2: String): GameFragment {
            val gameFragment = GameFragment()

            val args = Bundle()
            args.putString("player1", player1)
            args.putString("player2", player2)
            gameFragment.arguments = args

            return gameFragment
        }
    }

}
