package com.example.freelancer.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.freelancer.R
import com.example.freelancer.data.supabase
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var etFullName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText

    private lateinit var cbTerms: CheckBox

    private lateinit var btnCreateAccount: MaterialButton
    private lateinit var btnGoogle: MaterialButton
    private lateinit var btnLinkedIn: MaterialButton

    private lateinit var cvLoginRedirect: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signup)

        // ---------------------------------------------------------
        // Bind views
        // ---------------------------------------------------------

        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        cbTerms = findViewById(R.id.cbTerms)

        btnCreateAccount = findViewById(R.id.btnCreateAccount)
        btnGoogle = findViewById(R.id.btnGoogle)
        btnLinkedIn = findViewById(R.id.btnLinkedIn)

        cvLoginRedirect = findViewById(R.id.cvLoginRedirect)

        // ---------------------------------------------------------
        // Create Account
        // ---------------------------------------------------------

        btnCreateAccount.setOnClickListener {
            signup()
        }

        // ---------------------------------------------------------
        // Login redirect
        // ---------------------------------------------------------

        cvLoginRedirect.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            finish()
        }

        // ---------------------------------------------------------
        // Google Signup
        // ---------------------------------------------------------

        btnGoogle.setOnClickListener {

            Toast.makeText(
                this,
                "Google signup will be configured next",
                Toast.LENGTH_SHORT
            ).show()
        }

        // ---------------------------------------------------------
        // LinkedIn Signup
        // ---------------------------------------------------------

        btnLinkedIn.setOnClickListener {

            Toast.makeText(
                this,
                "LinkedIn signup will be configured next",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun signup() {

        val name = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        // ---------------------------------------------------------
        // Validation
        // ---------------------------------------------------------

        if (name.isEmpty()) {
            etFullName.error = "Enter your full name"
            etFullName.requestFocus()
            return
        }

        if (email.isEmpty()) {
            etEmail.error = "Enter your email"
            etEmail.requestFocus()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {
            etEmail.error = "Enter a valid email address"
            etEmail.requestFocus()
            return
        }

        if (password.length < 8) {
            etPassword.error = "Password must be at least 8 characters"
            etPassword.requestFocus()
            return
        }

        if (!password.any { it.isDigit() }) {
            etPassword.error = "Password must contain a number"
            etPassword.requestFocus()
            return
        }

        if (!password.any { !it.isLetterOrDigit() }) {
            etPassword.error = "Password must contain a symbol"
            etPassword.requestFocus()
            return
        }

        if (!cbTerms.isChecked) {

            Toast.makeText(
                this,
                "Please accept the Terms & Conditions and Privacy Policy",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        // ---------------------------------------------------------
        // Disable button during request
        // ---------------------------------------------------------

        btnCreateAccount.isEnabled = false

        lifecycleScope.launch {

            try {

                // Clear any previous locally stored session.
                // Important during development/testing.
                try {
                    supabase.auth.signOut()
                } catch (_: Exception) {
                    // No existing session - ignore.
                }

                // -------------------------------------------------
                // Supabase Signup
                // -------------------------------------------------

                supabase.auth.signUpWith(
                    Email,
                    redirectUrl = "profreelance://auth-callback"
                ) {
                    this.email = email
                    this.password = password
                }

                // -------------------------------------------------
                // Confirm Email is ON:
                // User should verify email before logging in.
                // -------------------------------------------------

                Toast.makeText(
                    this@SignupActivity,
                    "Account created. Please check your email and verify your account.",
                    Toast.LENGTH_LONG
                ).show()

                // Go back to Login.
                // DO NOT open MainActivity here.
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