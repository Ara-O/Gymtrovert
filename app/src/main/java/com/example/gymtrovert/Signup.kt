package com.example.gymtrovert

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gymtrovert.databinding.ActivitySignupBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    lateinit var btSignIn: SignInButton
    private val REQ_ONE_TAP = 2
    private var showOneTapUI = true
    private lateinit var firebaseAuth: FirebaseAuth
    private val TAG ="AAAAAAAA"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        Firebase.auth.signOut()
//        Check for current user

        val sharedPreferences = getSharedPreferences("RegistrationPrefs", MODE_PRIVATE)
        val isUserLoggedIn = sharedPreferences.getBoolean("isUserLoggedIn", false)

        if (isUserLoggedIn) {
            // User is signed in, do something here
            Log.d("wevwv" ,"user signed in!")
        } else {
            // User is not signed in, do something else here
            Log.d("wevwv" ,"user no signed in!")
        }

//        val username = binding.usernameInput.text
//        val email = binding.emailAddressInput.text
//        val password = binding.passwordInput.text
        btSignIn = binding.btSignIn

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_ID_TOKEN)
            .requestEmail()
            .build()
//
//        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        btSignIn.setOnClickListener { // Initialize sign in intent
            val intent: Intent = googleSignInClient.signInIntent
            // Start activity for result
            startActivityForResult(intent, 100)
        }
        firebaseAuth = FirebaseAuth.getInstance()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            100 -> {
                // When request code is equal to 100 initialize task
                val signInAccountTask: Task<GoogleSignInAccount> =  GoogleSignIn.getSignedInAccountFromIntent(data)

                // check condition
                if (signInAccountTask.isSuccessful) {
                    // When google sign in successful initialize string
                    val s = "Google sign in successful"
                    // Display Toast
                    displayToast(s)
                    // Initialize sign in account
                    try {
                        // Initialize sign in account
                        val googleSignInAccount =
                            signInAccountTask.getResult(ApiException::class.java)
                        // Check condition
                        if (googleSignInAccount != null) {
                            // When sign in account is not equal to null initialize auth credential
                            val authCredential: AuthCredential = GoogleAuthProvider.getCredential(
                                googleSignInAccount.idToken, null
                            )
                            // Check credential
                            firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this) { task ->
                                    // Check condition
                                    if (task.isSuccessful) {
                                        val sharedPreferences =
                                            getSharedPreferences("RegistrationPrefs", Context.MODE_PRIVATE)
                                        val editor = sharedPreferences.edit()
                                        editor.putBoolean("isUserLoggedIn", true)
                                        editor.apply()
                                        Log.d(
                                            "i hope",
                                            googleSignInAccount.idToken.toString() + " " + googleSignInAccount.email.toString()
                                        )

                                        // When task is successful redirect to profile activity

                                        displayToast("Firebase authentication successful")
                                    } else {
                                        // When task is unsuccessful display Toast
                                        displayToast(
                                            "Authentication Failed :" + task.exception?.message
                                        )
                                    }
                                }
                        }
                    } catch (e: ApiException) {
                        e.printStackTrace()
                    }
                }
            }

        }
    }
private fun displayToast(s: String) {
    Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
}
}