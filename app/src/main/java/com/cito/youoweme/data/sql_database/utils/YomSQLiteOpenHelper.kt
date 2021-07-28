package com.cito.youoweme.data.sql_database.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class YomSQLiteOpenHelper(context: Context?) : SQLiteOpenHelper(context,
    YomDBNames.DATABASE_NAME, null, 1) {

    companion object {
        const val CREATE_TRANS_QUERY = """
            CREATE TABLE ${YomDBNames.TRANSACTIONS_TABLE}(
                ${YomDBNames.TRANSACTIONS_COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${YomDBNames.TRANSACTIONS_COL_AMOUNT} FLOAT(2) NOT NULL,
                ${YomDBNames.TRANSACTIONS_COL_CONTACT_ID} INTEGER,
                ${YomDBNames.TRANSACTIONS_COL_DATE} BIGINT,
                ${YomDBNames.TRANSACTIONS_COL_TITLE} VARCHAR(256),
                ${YomDBNames.TRANSACTIONS_COL_DESCRIPTION} VARCHAR(256),
                FOREIGN KEY (${YomDBNames.TRANSACTIONS_COL_CONTACT_ID}) REFERENCES ${YomDBNames.TRANSACTIONS_TABLE}(${YomDBNames.CONTACTS_COL_ID})
            );
        """
        const val CREATE_CONTACTS_QUERY = """
            CREATE TABLE ${YomDBNames.CONTACTS_TABLE}(
                ${YomDBNames.CONTACTS_COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${YomDBNames.CONTACTS_COL_NAME} VARCHAR(256),
                ${YomDBNames.CONTACTS_COL_SURNAME} VARCHAR(256)
            );
        """
//        const val CREATE_USERS_QUERY = """
//            CREATE TABLE ${YomDBNames.USERS_TABLE}(
//                ${YomDBNames.USERS_COL_USERNAME} VARCHAR(256) PRIMARY KEY,
//                ${YomDBNames.USERS_COL_PASSWD_HASH} VARCHAR(256) NOT NULL
//            );
//        """

        const val DROP_TRANS_QUERY = "DROP TABLE IF EXISTS ${YomDBNames.TRANSACTIONS_TABLE};"
        const val DROP_CONTACTS_QUERY = "DROP TABLE IF EXISTS ${YomDBNames.CONTACTS_TABLE};"
//        const val DROP_USERS_QUERY = "DROP TABLE IF EXISTS ${YomDBNames.USERS_TABLE};"
    }

    override fun onCreate(db: SQLiteDatabase) {
        with(db) {
            execSQL(CREATE_TRANS_QUERY)
            execSQL(CREATE_CONTACTS_QUERY)
//            execSQL(CREATE_USERS_QUERY)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.i(
            YomSQLiteOpenHelper::class.simpleName,
            "Database Upgraded from version $oldVersion to version $newVersion");
        with(db) {
            execSQL(DROP_TRANS_QUERY)
            execSQL(DROP_CONTACTS_QUERY)
//            execSQL(DROP_USERS_QUERY)
            onCreate(this)
        }
    }
}