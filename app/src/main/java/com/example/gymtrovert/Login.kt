package com.example.gymtrovert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.gymtrovert.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.doesntHaveAccount.setOnClickListener {
            val i = Intent(this, Signup::class.java)
            startActivity(i)
        }

        val auth = Firebase.auth

        binding.loginButton.setOnClickListener {
            val email = binding.emailAddressInput.text
            val password = binding.passwordInput.text
            auth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Sign in success", "signInWithEmail:success")
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Sign in fail", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed - ${task.exception?.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}