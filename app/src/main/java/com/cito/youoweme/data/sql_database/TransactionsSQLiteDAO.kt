package com.cito.youoweme.data.sql_database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.cito.youoweme.data.YomDAO
import com.cito.youoweme.data.model.Transaction
import com.cito.youoweme.data.sql_database.utils.YomDBNames
import com.cito.youoweme.data.sql_database.utils.YomSQLiteOpenHelper


object TransactionsSQLiteDAO : YomDAO<Transaction> {

    private var yomSQLiteOpenHelper : SQLiteOpenHelper? = null
    private val db : SQLiteDatabase?
        get() = yomSQLiteOpenHelper?.writableDatabase

    override fun open(context: Context?) {
        Log.d(TransactionsSQLiteDAO::class.simpleName, "db opened")
        yomSQLiteOpenHelper = yomSQLiteOpenHelper ?: YomSQLiteOpenHelper(context)
    }

    override fun close() {
        Log.d(TransactionsSQLiteDAO::class.simpleName, "db closed")
        yomSQLiteOpenHelper?.close()
        yomSQLiteOpenHelper = null
    }

    override fun insert(value: Transaction): Boolean {
        val insID = db?.insert(
            YomDBNames.TRANSACTIONS_TABLE,
            null,
            value.toContentValues()
        )
        return (insID != null)
    }

    override fun delete(value: Transaction): Boolean {
        return db?.delete(
            YomDBNames.TRANSACTIONS_TABLE,
            "${YomDBNames.TRANSACTIONS_COL_ID} = ?",
            arrayOf("${value.id}")
        ) != null
    }

    override fun getAll(): List<Transaction>? {
        val transactions = arrayListOf<Transaction>()

        val cursor = db?.query(
            YomDBNames.TRANSACTIONS_TABLE,
            YomDBNames.TRANSACTIONS_COLS_LIST,
            null, null, null, null, null
        ) ?: return null

        cursor.apply {
            moveToFirst()
            while (!isAfterLast) {
                transactions.add(toTransaction())
                moveToNext()
            }
            close()
        }

        return transactions
    }

    override fun getById(id: Long): Transaction? {
        val cursor = db?.query(
            YomDBNames.TRANSACTIONS_TABLE,
            YomDBNames.TRANSACTIONS_COLS_LIST,
            "${YomDBNames.TRANSACTIONS_COL_ID} = $id",
            null, null, null, null
        ) ?: return null

        var transToReturn: Transaction? = null
        cursor.apply {
            moveToFirst()
            if (!isAfterLast)
                transToReturn = toTransaction()
            close()
        }
        return transToReturn
    }

    // Extension function
    private fun Transaction.toContentValues(): ContentValues {
        return ContentValues().apply {
            id?.let { put(YomDBNames.TRANSACTIONS_COL_ID, it) }
            put(YomDBNames.TRANSACTIONS_COL_AMOUNT, amount)
            put(YomDBNames.TRANSACTIONS_COL_CONTACT_ID, contactId)
            put(YomDBNames.TRANSACTIONS_COL_DATE, timeInMillis)
            put(YomDBNames.TRANSACTIONS_COL_TITLE, title)
            put(YomDBNames.TRANSACTIONS_COL_DESCRIPTION, desc)
        }
    }

    // Extension function
    private fun Cursor.toTransaction(): Transaction {
        return Transaction(
            getLong(0),
            getFloat(1),
            getLong(2),
            getLong(3),
            getString(4),
            getString(5),
        )
    }
}