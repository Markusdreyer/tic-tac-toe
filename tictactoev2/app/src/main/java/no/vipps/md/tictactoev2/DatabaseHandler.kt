package no.vipps.md.tictactoev2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_NAME = "TicTacToe"
val TABLE_NAME = "Leaderboard"
val COL_NAME = "name"
val COL_SCORE = "score"
val COL_ID = "id"

class DatabaseHandler(var context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " VARCHAR(256)," +
                COL_SCORE + " INTEGER)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertData(player: Player) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_NAME, player.name)
        cv.put(COL_SCORE, player.score)
        var result = db.insert(TABLE_NAME, null, cv)
        if(result == (-1).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    fun readData() : MutableList<Player> {
        var list : MutableList<Player> = ArrayList()

        var db = this.readableDatabase
        val query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_SCORE + " DESC"
        val result = db.rawQuery(query, null)

        if(result.moveToFirst()) {
            do {
                var user = Player()
                user.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                user.name = result.getString(result.getColumnIndex(COL_NAME))
                user.score = result.getString(result.getColumnIndex(COL_SCORE)).toInt()
                list.add(user)
            } while (result.moveToNext())
    }

        result.close()
        db.close()
        return list
    }

    fun incrementScore(player: String) {
        var db = this.writableDatabase
        val query = "SELECT * FROM " + TABLE_NAME
        val result = db.rawQuery(query, null)

        if(result.moveToFirst()) {
            do {
                var cv = ContentValues()
                cv.put(COL_SCORE, result.getInt(result.getColumnIndex(COL_SCORE)) +1)
                db.update(TABLE_NAME, cv, "$COL_NAME=?",
                    arrayOf(player))
            } while (result.moveToNext())


        }

        result.close()
        db.close()
    }

    fun checkIfPlayerExists(playerName: String) : Boolean {
        var db = this.readableDatabase
        val query = "SELECT $COL_NAME FROM " + TABLE_NAME
        val result = db.rawQuery(query, null)

        if(result.moveToFirst()) {
            do {
                if(result.getString(result.getColumnIndex(COL_NAME)) == playerName)
                return true
            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return false
    }

}
