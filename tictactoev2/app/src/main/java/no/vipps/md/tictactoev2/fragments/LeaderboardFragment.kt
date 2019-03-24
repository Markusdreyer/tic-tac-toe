package no.vipps.md.tictactoev2.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import no.vipps.md.tictactoev2.DatabaseHandler
import no.vipps.md.tictactoev2.LeaderboardRecyclerViewAdapter
import no.vipps.md.tictactoev2.PlayerObject
import no.vipps.md.tictactoev2.R

class LeaderboardFragment : DialogFragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_leaderboard_list, container, false)

        populateLeaderboard()

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> android.support.v7.widget.LinearLayoutManager(context)
                    else -> android.support.v7.widget.GridLayoutManager(context, columnCount)
                }
                adapter = LeaderboardRecyclerViewAdapter(
                    PlayerObject.ITEMS,
                    listener
                )
            }
        }
        return view
    }

    private fun populateLeaderboard() {
        val db = DatabaseHandler(activity!!.applicationContext)
        val data = db.readData()

        for(i in 0..(data.size -1)) {
            PlayerObject.addPlayer(
                PlayerObject.createPlayer(
                    data.get(i).name,
                    data.get(i).score
                )
            )
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        PlayerObject.ITEMS.clear() //To avoid duplication of entries in the list
    }

    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: PlayerObject.Player?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            LeaderboardFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
