package com.cito.youoweme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cito.youoweme.login.UserLoginManager
import com.cito.youoweme.login.model.User
import com.cito.youoweme.ui.theme.YouOweMeTheme

class RegistrationActivity : AppCompatActivity() {

//    private lateinit var binding: ActivityRegistrationBinding

    private lateinit var userLoginManager: UserLoginManager

    private var username: String? = null
    private var password: String? = null
    private var passwordConfirm: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityRegistrationBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        userLoginManager = UserLoginManager(this)

        setContent {
            YouOweMeTheme {
                RegistrationForm()
            }
        }
    }

    private fun checkInput(): Boolean {
        if (username.isNullOrEmpty()) {
            Toast.makeText(this, R.string.message_username_not_valid, Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isNullOrEmpty()) {
            Toast.makeText(this, R.string.message_password_invalid, Toast.LENGTH_SHORT).show()
            return false
        }
        if (passwordConfirm.isNullOrEmpty() || passwordConfirm != password) {
            Toast.makeText(this, R.string.message_password_confirmation_not_match, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun registerBtnClick(): Boolean {
        if (!checkInput()) return false

        return userLoginManager.register(
            User(
//                binding.edittextUsername.text.toString(),
//                binding.edittextPassword.text.toString().hashCode()
                username = username!!,
                passwordHash = password.hashCode(),
            )
        ).also {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun RegistrationForm() {
        val modifier = Modifier.fillMaxWidth()
        YouOweMeTheme {
            Column(modifier = modifier
                .fillMaxHeight()
                .padding(8.dp)) {
                Column(
                    modifier = modifier.weight(1f)
                ) {

//                    Spacer(modifier = modifier.height(100.dp))

                    val usernameInput = remember { mutableStateOf("") }
                    val passwordInput = remember { mutableStateOf("") }
                    val passwordConfirmInput = remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = usernameInput.value,
                        onValueChange = { usernameInput.value = it; username = it },
                        label = { Text(text = "Username") },
                        singleLine = true,
                        modifier = modifier,
                    )
                    OutlinedTextField(
                        value = passwordInput.value,
                        onValueChange = { passwordInput.value = it; password = it },
                        label = { Text(text = stringResource(R.string.word_password)) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = modifier.padding(top = 8.dp),
                    )
                    OutlinedTextField(
                        value = passwordConfirmInput.value,
                        onValueChange = { passwordConfirmInput.value = it; passwordConfirm = it },
                        label = { Text(text = stringResource(R.string.word_password_confirm))},
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = modifier.padding(top = 8.dp),
                    )
                }
                Row(modifier = modifier) {
                    Button(modifier = modifier,
                        onClick = this@RegistrationActivity::registerBtnClick
                    ) {
                        Text(text = "Register")
                    }
                }
            }

        }

    }
}