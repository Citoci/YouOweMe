package com.cito.youoweme.utils

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cito.youoweme.R
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.data.sql_database.TransactionsSQLiteDAO
import com.cito.youoweme.login.UserLoginManager

class YomApplication : Application() {
//    companion object {
//        var context : Context? = null
//            private set
//    }

    override fun onCreate() {
        super.onCreate()

        UserLoginManager(this).apply {
            loadLogin()
        }

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

fun quickToast(context: Context?, text: CharSequence) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun confirmActionWithDialog(context: Context, title: String, message: String? = null, action: () -> Unit) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(R.string.action_confirm) { dialog, which ->
            action()
        }
        .setNegativeButton(R.string.action_cancel, null)
        .show()
}