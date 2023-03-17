package com.example.gymtrovert

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gymtrovert.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.doesntHaveAccount.setOnClickListener {
            val i = Intent(this, Signup::class.java)
            startActivity(i)
            finish()
            overridePendingTransition(
                com.example.gymtrovert.R.anim.slide_in_left,
                com.example.gymtrovert.R.anim.slide_out_right);
        }

        val auth = Firebase.auth
        database = Firebase.database.reference


        binding.loginButton.setOnClickListener {
            val email = binding.emailAddressInput.text
            val password = binding.passwordInput.text
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sign in success", "signInWithEmail:success")

                            //Put user id in sharedpreferences
                            val sharedPreferences =
                                getSharedPreferences("sharedPreferences", MODE_PRIVATE)
                            val firebasePref = sharedPreferences.edit()
                            firebasePref.putString("id", auth.currentUser?.uid.toString())
                            firebasePref.apply()

                            //Get user's username and store in sharedpreferences
                            database.child("/${auth.currentUser?.uid}").child("/username").get().addOnSuccessListener {
                                firebasePref.putString("username", it.value.toString())
                                firebasePref.apply()
                            }.addOnFailureListener{
                                Log.e("firebase", "Error getting data", it)
                            }
                            val i = Intent(this, MainActivity::class.java)
                            startActivity(i)
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Sign in fail", "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed - ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Some fields may be missing, please try again",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            com.example.gymtrovert.R.anim.slide_in_left,
            com.example.gymtrovert.R.anim.slide_out_right);
    }
}