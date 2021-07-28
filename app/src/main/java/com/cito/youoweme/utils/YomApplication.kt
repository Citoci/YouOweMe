package com.cito.youoweme.utils

import android.app.Application
import android.content.Context
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.data.sql_database.TransactionsSQLiteDAO

class YomApplication : Application() {
//    companion object {
//        var context : Context? = null
//            private set
//    }

    override fun onCreate() {
        super.onCreate()

//        context = applicationContext
//        TransactionsSQLiteDAO.open(applicationContext)
//        ContactsSQLiteDAO.open(applicationContext)

    }

    override fun onTerminate() {
        TransactionsSQLiteDAO.close()
        ContactsSQLiteDAO.close()

        super.onTerminate()
    }
}