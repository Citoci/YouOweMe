package com.cito.youoweme.data.sql_database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.cito.youoweme.data.YomDAO
import com.cito.youoweme.data.model.Contact
import com.cito.youoweme.data.sql_database.utils.YomDBNames
import com.cito.youoweme.data.sql_database.utils.YomSQLiteOpenHelper


object ContactsSQLiteDAO : YomDAO<Contact, Long> {

    private var yomSQLiteOpenHelper : SQLiteOpenHelper? = null
    private val db : SQLiteDatabase?
        get() = yomSQLiteOpenHelper?.writableDatabase

    override fun open(context: Context?) {
        yomSQLiteOpenHelper = yomSQLiteOpenHelper ?: YomSQLiteOpenHelper(context)
    }

    override fun close() {
        yomSQLiteOpenHelper?.close()
        yomSQLiteOpenHelper = null
    }

    override fun insert(value: Contact): Boolean {
        val insID = db?.insert(
            YomDBNames.CONTACTS_TABLE,
            null,
            value.toContentValues()
        )
        return (insID != null)
    }

    override fun delete(value: Contact): Boolean {
        return db?.delete(
            YomDBNames.CONTACTS_TABLE,
            "${YomDBNames.CONTACTS_COL_ID} = ?",
            arrayOf("${value.id}")
        ) != null
    }

    override fun getAll(): List<Contact>? {
        val contacts = arrayListOf<Contact>()

        val cursor = db?.query(
            YomDBNames.CONTACTS_TABLE,
            YomDBNames.CONTACTS_COLS_LIST,
            null, null, null, null, null
        ) ?: return null

        cursor.apply {
            moveToFirst()
            while (!isAfterLast) {
                contacts.add(toContact())
                moveToNext()
            }
            close()
        }

        return contacts
    }

    override fun getById(id: Long): Contact? {
        val cursor = db?.query(
            YomDBNames.CONTACTS_TABLE,
            YomDBNames.CONTACTS_COLS_LIST,
            "${YomDBNames.CONTACTS_COL_ID} = $id",
            null, null, null, null
        ) ?: return null

        var contToReturn: Contact? = null
        cursor.apply {
            moveToFirst()
            if (!isAfterLast)
                contToReturn = toContact()
            close()
        }
        return contToReturn
    }

    private fun Contact.toContentValues(): ContentValues {
        val values = ContentValues()
        id?.let { values.put(YomDBNames.CONTACTS_COL_ID, it) }
        values.put(YomDBNames.CONTACTS_COL_NAME, name)
        values.put(YomDBNames.CONTACTS_COL_SURNAME, surname)
        values.put(YomDBNames.CONTACTS_COL_USERNAME, usernameRef)
        return values
    }

    private fun Cursor.toContact(): Contact {
        return Contact(
            id = getLong(0),
            name = getString(1),
            surname = getString(2),
            usernameRef = getString(3)
        )
    }
}