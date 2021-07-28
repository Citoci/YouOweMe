package com.cito.youoweme.data.sql_database.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
/*
class UsersSQLiteOpenHelper(context: Context?) : SQLiteOpenHelper(context,
    YomDBNames.USERS_DATABASE_NAME, null, 1) {

    companion object {
        const val CREATE_USERS_QUERY = """
            CREATE TABLE ${YomDBNames.USERS_TABLE}(
                ${YomDBNames.USERS_COL_USERNAME} VARCHAR(256) PRIMARY KEY,
                ${YomDBNames.USERS_COL_PASSWD_HASH} VARCHAR(256) NOT NULL
            );
        """

        const val DROP_USERS_QUERY = "DROP TABLE IF EXISTS ${YomDBNames.USERS_TABLE};"
    }

    override fun onCreate(db: SQLiteDatabase) {
        with(db) {
            execSQL(CREATE_USERS_QUERY)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.i(
            UsersSQLiteOpenHelper::class.simpleName,
            "Database Upgraded from version $oldVersion to version $newVersion");
        with(db) {
            execSQL(DROP_USERS_QUERY)
            onCreate(this)
        }
    }
}

 */