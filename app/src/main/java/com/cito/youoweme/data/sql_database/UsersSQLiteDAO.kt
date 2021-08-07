package com.cito.youoweme.data.sql_database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.cito.youoweme.data.YomDAO
import com.cito.youoweme.data.sql_database.utils.YomDBNames
import com.cito.youoweme.data.sql_database.utils.YomSQLiteOpenHelper
import com.cito.youoweme.login.model.User


object UsersSQLiteDAO : YomDAO<User, String> {

    private var usersSQLiteOpenHelper : SQLiteOpenHelper? = null
    private val db : SQLiteDatabase?
        get() = usersSQLiteOpenHelper?.writableDatabase

    override fun open(context: Context?) {
        usersSQLiteOpenHelper = usersSQLiteOpenHelper ?: YomSQLiteOpenHelper(context)
    }

    override fun close() {
        usersSQLiteOpenHelper?.close()
    }

    override fun insert(value: User): Boolean {
        val insID = db?.insert(
            YomDBNames.USERS_TABLE,
            null,
            value.toContentValues()
        )
        return (insID != null)
    }

    override fun delete(value: User): Boolean {
        return db?.delete(
            YomDBNames.USERS_TABLE,
            "${YomDBNames.USERS_COL_USERNAME} = ?",
            arrayOf(value.username)
        ) != null
    }

    override fun getAll(): List<User>? {
        val users = arrayListOf<User>()

        val cursor = db?.query(
            YomDBNames.USERS_TABLE,
            YomDBNames.USERS_COLS_LIST,
            null, null, null, null, null
        ) ?: return null

        cursor.apply {
            moveToFirst()
            while (!isAfterLast) {
                users.add(toUser())
                moveToNext()
            }
            close()
        }

        return users
    }

    override fun getById(id: String): User? {
        val cursor = db?.query(
            YomDBNames.USERS_TABLE,
            YomDBNames.USERS_COLS_LIST,
            "${YomDBNames.USERS_COL_USERNAME} = $id",
            null, null, null, null
        ) ?: return null

        var userToReturn: User? = null
        cursor.apply {
            moveToFirst()
            if (!isAfterLast)
                userToReturn = toUser()
            close()
        }
        return userToReturn
    }

    private fun User.toContentValues(): ContentValues {
        val values = ContentValues()
        values.put(YomDBNames.USERS_COL_USERNAME, username)
        values.put(YomDBNames.USERS_COL_PASSWD_HASH, passwordHash)
        return values
    }

    private fun Cursor.toUser(): User {
        return User(getString(0), getInt(1))
    }

//    fun getById(id: Long): User? = null
}



