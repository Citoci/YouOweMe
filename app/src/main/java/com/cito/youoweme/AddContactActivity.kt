package com.cito.youoweme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.cito.youoweme.data.model.Contact
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.login.UserLoginManager
import java.util.*

class AddContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        findViewById<Button>(R.id.btn_save_new_contact).setOnClickListener {
            if (!checkInput()) return@setOnClickListener

            ContactsSQLiteDAO.insert(Contact(
                name = findViewById<EditText>(R.id.edittext_contact_name).text.toString(),
                surname = findViewById<EditText>(R.id.edittext_contact_surname).text.toString(),
                usernameRef = UserLoginManager.loggedUsername
            ))
            finish()
        }
    }

    private fun checkInput(): Boolean {
        return true
    }
}