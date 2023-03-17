package com.example.gymtrovert

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gymtrovert.databinding.ActivitySignupBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseError
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize


class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var btSignIn: SignInButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get current user id if exists
        val storedData = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        val sharedPreferencesId = storedData.getString("id", "")

        if(sharedPreferencesId!!.isNotEmpty()){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }


         //Check if user exists
         auth = Firebase.auth

        btSignIn = binding.btSignIn

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_ID_TOKEN)
            .requestEmail()
            .build()

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        btSignIn.setOnClickListener { // Initialize sign in intent
            val intent: Intent = googleSignInClient.signInIntent
            // Start activity for result
            startActivityForResult(intent, 100)
        }


         binding.signupButton.setOnClickListener {
             val username = binding.usernameInput.text
             val emailAddress = binding.emailAddressInput.text
             val password = binding.passwordInput.text

             if(emailAddress.isNotEmpty() && password.isNotEmpty()){
                 auth.createUserWithEmailAndPassword(emailAddress.toString(), password.toString())
                     .addOnCompleteListener(this) { task ->
                         if (task.isSuccessful) {
                             // Sign in success, update UI with the signed-in user's information
                             Log.d("Success", "createUserWithEmail:success")
                             displayToast("Successfully signed in")
                             val user = auth.currentUser

                             //Save user's username
                             val database = Firebase.database
                             val userUsername = database.getReference("/${user?.uid}/username")
                             userUsername.setValue(username.toString())

                             //Store user id using sharedPreference
                             val sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
                             val firebasePref = sharedPreferences.edit()
                             firebasePref.putString("id", user?.uid.toString())
                             firebasePref.putString("username", username.toString())
                             firebasePref.apply()

                             val i = Intent(this, MainActivity::class.java)
                             startActivity(i)
                             overridePendingTransition(R.anim.slide_in_right,
                                 R.anim.slide_out_left);
                             finish()
                         } else {
                             // If sign in fails, display a message to the user.
                             Log.w("Failure", "createUserWithEmail:failure", task.exception)
                             displayToast("Authentication Failed: ${task.exception?.message.toString()}")
                         }
                     }
             }else{
                 displayToast("Some fields may be missing, please try again")
             }
         }

         binding.alreadyHasAccount.setOnClickListener {
             val i = Intent(this, Login::class.java)
             startActivity(i)
             overridePendingTransition(
                 R.anim.slide_in_right,
                 R.anim.slide_out_left)
             finish()
         }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            100 -> {
                val username = binding.usernameInput.text
                // When request code is equal to 100 initialize task
                val signInAccountTask: Task<GoogleSignInAccount> =  GoogleSignIn.getSignedInAccountFromIntent(data)

                // check condition
                if (signInAccountTask.isSuccessful) {
                    try {
                        // Initialize sign in account
                        val googleSignInAccount =
                            signInAccountTask.getResult(ApiException::class.java)

                        if (googleSignInAccount != null) {

                            val authCredential: AuthCredential = GoogleAuthProvider.getCredential(
                                googleSignInAccount.idToken, null
                            )
                            // Gets credential from google sign in and uses the credential to sign in to firebase
                            auth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this) { task ->
                                    // Check condition
                                    if (task.isSuccessful) {
                                        //Save user's username
                                        val database = Firebase.database
                                        val userUsername = database.getReference("/${auth.currentUser?.uid}/username")
                                        userUsername.setValue(username)


                                        //Store user id using sharedPreference
                                        val sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
                                        val firebasePref = sharedPreferences.edit()
                                        firebasePref.putString("id", auth.currentUser?.uid.toString())
                                        firebasePref.putString("username", username.toString())
                                        firebasePref.apply()


                                        val i = Intent(this, MainActivity::class.java)
                                        startActivity(i)
                                        overridePendingTransition(R.anim.slide_in_right,
                                            R.anim.slide_out_left);
                                        finish()
                                        // When task is successful redirect to profile activity
                                        displayToast("Authentication successful ${auth.currentUser?.uid}")

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