package com.cito.youoweme.login

import android.content.Context
import com.cito.youoweme.login.model.User

/*
object LoggedUser {
     private const val LOGGED_USERNAME_KEY = "com.cito.youoweme.login.LoggedUser.LOGGED_USERNAME_KEY"

    var username: String? = null
        private set

    fun saveLogin(context: Context) {
        context.getSharedPreferences(LoggedUser::class.simpleName, Context.MODE_PRIVATE).edit().apply {
            putString(LOGGED_USERNAME_KEY, username)
            apply()
        }
    }

    fun loadLogin(context: Context) {
        context.getSharedPreferences(LoggedUser::class.simpleName, Context.MODE_PRIVATE).apply {
            username = getString(LOGGED_USERNAME_KEY, null)
        }
    }

    fun login(context: Context, newUsername: String) {
        username = newUsername
        saveLogin(context)
    }

    fun logout(context: Context) {
        username = null
        context.getSharedPreferences(LoggedUser::class.simpleName, Context.MODE_PRIVATE).edit().apply {
            remove(LOGGED_USERNAME_KEY)
            apply()
        }
    }

}

 */