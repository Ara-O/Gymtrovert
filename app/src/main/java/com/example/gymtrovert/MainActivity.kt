package com.example.gymtrovert

import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.example.gymtrovert.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class loggedData(
   val numOfGymGoers: Int,
   val loggedTime: String,
   val loggedWeight: Int
)
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var  authListener: AuthStateListener
    private lateinit   var user : FirebaseUser

    override fun onDestroy() {
        super.onDestroy()
        Firebase.auth.removeAuthStateListener(authListener)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Check users registration status
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            user = firebaseAuth.currentUser!!
            if (user != null) {
                // User is signed in
                Log.d("vewfwe", "user already exists")

            } else {
                val i = Intent(this, Signup::class.java)
                startActivity(i)
                finish()
            }
        }

        Firebase.auth.addAuthStateListener(authListener)



        var numOfPeopleInGym = 0

        binding.increaseNumOfPeople.setOnClickListener {
            numOfPeopleInGym++
            binding.numOfPeopleInTheGym.text = numOfPeopleInGym.toString()

            if(numOfPeopleInGym == 1)
                binding.peopleText.text = "person"
            else
                binding.peopleText.text = "people"

            if(numOfPeopleInGym > 10){
                Toast.makeText(applicationContext, "Geez, I feel bad for you :(", Toast.LENGTH_SHORT).show()
            }
        }

        binding.reduceNumOfPeople.setOnClickListener {
            if(numOfPeopleInGym > 0){
                numOfPeopleInGym--
            }
            binding.numOfPeopleInTheGym.text = numOfPeopleInGym.toString()

            if(numOfPeopleInGym == 1)
                binding.peopleText.text = "person"
            else
                binding.peopleText.text = "people"
        }

        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        binding.hour.setText( hour.toString())
        binding.min.setText(minute.toString())


        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_ID_TOKEN)
            .requestEmail()
            .build()

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)


        val database = Firebase.database
        binding.logData.setOnClickListener {
//            val c = Calendar.getInstance()
//            DayofWeek-hour-minute
            //Monday-09-03
            //Monday-09
//            val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
//            val minute = c.get(Calendar.MINUTE)

//            var loggedTime = ""
//            loggedTime += "${}"
//            Get current date and time set that under the time
//                val data = loggedData(binding.numOfPeopleInTheGym.text.toString().toInt(), )
//            val userUsername = database.getReference("/${user?.uid}/username")
//
//            userUsername.setValue(.toString())
        }

        binding.logOut.setOnClickListener {

            Firebase.auth.signOut()
            Auth.GoogleSignInApi.signOut(googleSignInClient.asGoogleApiClient());
            GoogleSignIn.getClient(
                applicationContext,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            ).signOut()

        }

    }
}