package com.example.freelancer.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.freelancer.ui.HomeActivity
import com.example.freelancer.R
import com.example.freelancer.data.supabase
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    private lateinit var btnLogin: MaterialButton
    private lateinit var btnGoogle: MaterialButton
    private lateinit var btnLinkedIn: MaterialButton

    private lateinit var cvSignUpRedirect: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        // ---------------------------------------------------------
        // Bind views
        // ---------------------------------------------------------

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        btnLogin = findViewById(R.id.btnLogin)
        btnGoogle = findViewById(R.id.btnGoogle)
        btnLinkedIn = findViewById(R.id.btnLinkedIn)

        cvSignUpRedirect = findViewById(R.id.cvSignUpRedirect)

        // ---------------------------------------------------------
        // Login
        // ---------------------------------------------------------

        btnLogin.setOnClickListener {
            login()
        }

        // ---------------------------------------------------------
        // Go to Signup
        // ---------------------------------------------------------

        cvSignUpRedirect.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SignupActivity::class.java
                )
            )
        }

        // ---------------------------------------------------------
        // Google
        // ---------------------------------------------------------

        btnGoogle.setOnClickListener {

            Toast.makeText(
                this,
                "Google login will be configured next",
                Toast.LENGTH_SHORT
            ).show()
        }

        // ---------------------------------------------------------
        // LinkedIn
        // ---------------------------------------------------------

        btnLinkedIn.setOnClickListener {

            Toast.makeText(
                this,
                "LinkedIn login will be configured next",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun login() {

        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        // ---------------------------------------------------------
        // Validation
        // ---------------------------------------------------------

        if (email.isEmpty()) {
            etEmail.error = "Enter your email"
            etEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            etPassword.error = "Enter your password"
            etPassword.requestFocus()
            return
        }

        // ---------------------------------------------------------
        // Disable button while request is running
        // ---------------------------------------------------------

        btnLogin.isEnabled = false

        lifecycleScope.launch {

            try {

                supabase.auth.signInWith(Email) {

                    this.email = email
                    this.password = password
                }

                Toast.makeText(
                    this@LoginActivity,
                    "Login successful",
                    Toast.LENGTH_SHORT
                ).show()

                // -------------------------------------------------
                // Go to the main application
                // -------------------------------------------------

                startActivity(
                    Intent(
                        this@LoginActivity,
                        HomeActivity::class.java
                    ).apply {
                        flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                )

                finish()

            } catch (e: Exception) {

                val errorMessage = e.message ?: "Login failed"

                Toast.makeText(
                    this@LoginActivity,
                    errorMessage,
                    Toast.LENGTH_LONG
                ).show()

                btnLogin.isEnabled = true
            }
        }
    }
}