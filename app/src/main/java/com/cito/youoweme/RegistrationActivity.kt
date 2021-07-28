package com.cito.youoweme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.data.sql_database.TransactionsSQLiteDAO
import com.cito.youoweme.databinding.ActivityRegistrationBinding
import com.cito.youoweme.login.UserLoginManager
import com.cito.youoweme.login.model.User

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    private lateinit var userLoginManager: UserLoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userLoginManager = UserLoginManager(this)

        binding.btnRegistrationSubmit.setOnClickListener {
            if (userLoginManager.register(
                User(
                    binding.edittextUsername.text.toString(),
                    binding.edittextPassword.text.toString().hashCode()
                )
            )) {
//                TransactionsSQLiteDAO.open(this)
//                ContactsSQLiteDAO.open(this)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun check(): Boolean {
        return true
    }
}