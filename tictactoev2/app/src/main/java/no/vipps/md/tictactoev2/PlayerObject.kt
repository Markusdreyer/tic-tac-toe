package no.vipps.md.tictactoev2

import java.util.ArrayList
import java.util.HashMap

object PlayerObject {
    val ITEMS: MutableList<Player> = ArrayList()
    private val ITEM_MAP: MutableMap<String, Player> = HashMap()

    fun addPlayer(item: Player) {
        ITEMS.add(item)
        ITEM_MAP[item.id] = item
    }

    fun createPlayer(name: String, score: Int): Player {
        return Player(name, score.toString())
    }

    data class Player(val id: String, val content: String) {
        override fun toString(): String = content
    }

}
