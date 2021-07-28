package com.cito.youoweme.data.sql_database.utils

import com.cito.youoweme.login.UserLoginManager

object YomDBNames {
    val DATABASE_NAME
        get() = "yom_db_" + (UserLoginManager.loggedUser?.username ?: "guest")

    const val TRANSACTIONS_TABLE = "transactions"
    const val TRANSACTIONS_COL_ID = "_id"
    const val TRANSACTIONS_COL_AMOUNT = "amount"
    const val TRANSACTIONS_COL_CONTACT_ID = "contact_id"
    const val TRANSACTIONS_COL_DATE = "date"
    const val TRANSACTIONS_COL_TITLE = "title"
    const val TRANSACTIONS_COL_DESCRIPTION = "description"

    val TRANSACTIONS_COLS_LIST = arrayOf(
        TRANSACTIONS_COL_ID,
        TRANSACTIONS_COL_AMOUNT,
        TRANSACTIONS_COL_CONTACT_ID,
        TRANSACTIONS_COL_DATE,
        TRANSACTIONS_COL_TITLE,
        TRANSACTIONS_COL_DESCRIPTION,
    )

    const val CONTACTS_TABLE = "contacts"
    const val CONTACTS_COL_ID = "_id"
    const val CONTACTS_COL_NAME = "name"
    const val CONTACTS_COL_SURNAME = "surname"

    val CONTACTS_COLS_LIST = arrayOf(CONTACTS_COL_ID, CONTACTS_COL_NAME, CONTACTS_COL_SURNAME)

//    val USERS_DATABASE_NAME = "yom_users_db"
//
//    const val USERS_TABLE = "users"
//    const val USERS_COL_USERNAME = "username"
//    const val USERS_COL_PASSWD_HASH = "password_hash"
//
//    val USERS_COLS_LIST = arrayOf(USERS_COL_USERNAME, USERS_COL_PASSWD_HASH)

}