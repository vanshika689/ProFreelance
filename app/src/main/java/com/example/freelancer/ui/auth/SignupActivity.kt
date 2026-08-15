package com.example.freelancer.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.freelancer.R
import com.example.freelancer.data.supabase
import com.example.freelancer.ui.HomeActivity
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnCreateAccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnCreateAccount = findViewById(R.id.btnCreateAccount)

        btnCreateAccount.setOnClickListener {
            signup()
        }
    }

    private fun signup() {

        val name = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        if (name.isEmpty()) {
            etFullName.error = "Enter your name"
            return
        }

        if (email.isEmpty()) {
            etEmail.error = "Enter your email"
            return
        }

        if (password.length < 6) {
            etPassword.error = "Password must be at least 6 characters"
            return
        }



        btnCreateAccount.isEnabled = false

        lifecycleScope.launch {
            try {

                // Make absolutely sure no old session is hanging around
                try {
                    supabase.auth.signOut()
                } catch (_: Exception) {
                    // Ignore if there was no existing session
                }

                supabase.auth.signUpWith(
                    Email,
                    redirectUrl = "profreelance://auth-callback"
                ) {
                    this.email = email
                    this.password = password
                }

                Toast.makeText(
                    this@SignupActivity,
                    "Account created. Please check your email and verify your account.",
                    Toast.LENGTH_LONG
                ).show()

                // Go to login ONLY.
                // Do NOT go to Home here.
                startActivity(
                    Intent(
                        this@SignupActivity,
                        LoginActivity::class.java
                    )
                )

                finish()

            } catch (e: Exception) {

                Toast.makeText(
                    this@SignupActivity,
                    e.message ?: "Signup failed",
                    Toast.LENGTH_LONG
                ).show()

                btnCreateAccount.isEnabled = true
            }
        }
    }
}