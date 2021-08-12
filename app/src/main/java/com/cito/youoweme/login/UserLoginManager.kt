package com.cito.youoweme.login

import android.content.Context
import android.util.Log
import com.cito.youoweme.data.sql_database.UsersSQLiteDAO
import com.cito.youoweme.login.model.User

class UserLoginManager(val context: Context) {

    companion object {
//        private const val REGISTERED_USERS_PREFERENCES =
//            "com.cito.youoweme.login.UsersManager.REGISTERED_USERS_PREFERENCES"
        private const val LOGGED_USER_PREFERENCES =
            "com.cito.youoweme.login.UsersManager.LOGGED_USER_PREFERENCES"
        private const val LOGGED_USERNAME_KEY = "com.cito.youoweme.login.UsersManager.LOGGED_USERNAME_KEY"
        private const val LOGGED_PASSWD_HASH_KEY = "com.cito.youoweme.login.UsersManager.LOGGED_PASSWD_HASH_KEY"

        var loggedUser: User? = null
        val loggedUsername get() = loggedUser?.username
//    private const val LOGGED_AS_GUEST_KEY = "com.cito.youoweme.login.UsersManager.LOGGED_AS_GUEST_KEY"
    }

//    lateinit var context: Context

    fun register(user: User): Boolean {
//        with(context.getSharedPreferences(REGISTERED_USERS_PREFERENCES, Context.MODE_PRIVATE)) {
//            if (getInt(user.username, -1) == -1) {
//                with(edit()) {
//                    putInt(user.username, user.passwordHash)
//                    apply()
//                }
//                login(user.username, user.passwordHash)
//                return true
//            }
//        }
//        Log.d(UserLoginManager::class.simpleName, "User not registered, username already exists")
//        return false
        with(UsersSQLiteDAO) {
            open(context)
            insert(user).also {
                close()
                return it
            }
        }
    }

    fun unregister(user: User): Boolean {
//        with(context.getSharedPreferences(REGISTERED_USERS_PREFERENCES, Context.MODE_PRIVATE)) {
//            if (getInt(user.username, -1) == -1) {
//                Log.d(UserLoginManager::class.simpleName, "User not removed, username doesn't exists")
//                return false
//            }
//            with(edit()) {
//                remove(user.username)
//                apply()
//            }
//            return true
//        }
        with(UsersSQLiteDAO) {
            open(context)
            delete(user).also {
                close()
                return it
            }
        }
    }

    fun login(username: String, passwdHash: Int): Boolean {
        UsersSQLiteDAO.open(context)
        if (UsersSQLiteDAO.getById(username)?.passwordHash != passwdHash) {
            Log.d(UserLoginManager::class.simpleName, "Login attempt failed, wrong credentials")
            return false
        }
        UsersSQLiteDAO.close()
        loggedUser = User(username, passwdHash)
        storeLogin()
        return true
    }

    fun logout() {
        loggedUser = null
        storeLogin()
    }

    fun storeLogin() {
        with(context.getSharedPreferences(LOGGED_USER_PREFERENCES, Context.MODE_PRIVATE).edit()) {
            if (loggedUser == null) {
                remove(LOGGED_USERNAME_KEY)
                remove(LOGGED_PASSWD_HASH_KEY)
            } else {
                putString(LOGGED_USERNAME_KEY, loggedUser!!.username)
                putInt(LOGGED_PASSWD_HASH_KEY, loggedUser!!.passwordHash)
            }
            apply()
        }
    }

    fun loadLogin() {
        with(context.getSharedPreferences(LOGGED_USER_PREFERENCES, Context.MODE_PRIVATE)) {
            getString(LOGGED_USERNAME_KEY, null)?.let {
                login(it, getInt(LOGGED_PASSWD_HASH_KEY, -2))
            }
        }
    }

//    fun getLoggedUsername() = context.getSharedPreferences(LOGGED_USER_PREFERENCES, Context.MODE_PRIVATE).getString(LOGGED_USERNAME_KEY, null)

}