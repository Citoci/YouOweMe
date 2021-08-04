package com.cito.youoweme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.data.sql_database.TransactionsSQLiteDAO
import com.cito.youoweme.login.UserLoginManager

class LoginActivity : AppCompatActivity() {

    private lateinit var usersLoginManager: UserLoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        usersLoginManager = UserLoginManager(this).apply {
            loadLogin()
        }

        if (UserLoginManager.loggedUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        usersDAO = UsersSQLiteDAO(context)

        findViewById<Button>(R.id.btn_login_submit).setOnClickListener {
            val usernameInput = findViewById<EditText>(R.id.input_username).text.toString()
            val passwdHashInput = findViewById<EditText>(R.id.input_password).text.toString().hashCode()
            if (usersLoginManager.login(usernameInput, passwdHashInput)) {
//                TransactionsSQLiteDAO.open(this)
//                ContactsSQLiteDAO.open(this)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, R.string.message_wrong_credentials, Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<TextView>(R.id.btn_goto_registration).setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        findViewById<Button>(R.id.btn_skip_login).setOnClickListener {
//            TransactionsSQLiteDAO.open(this)
//            ContactsSQLiteDAO.open(this)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    // Now done in the login manager
//    private fun checkCredentials(username: String, passwdHash: Int): Boolean {
//        UsersSQLiteDAO.run {
////            Log.d(LoginActivity::class.simpleName, "db open")
////            insert(User("user1", "user1".hashCode()))
//            getAll()?.forEach {
//                if (it.username == username && it.passwordHash == passwdHash)
//                    return true
//            }
//        }
//        return false
//    }

}