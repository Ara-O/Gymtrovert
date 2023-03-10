package com.example.gymtrovert

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.gymtrovert.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

data class LoggedData(
   val numOfGymGoers: Int,
   val loggedTime: String,
   val loggedWeight: Int
)
class MainActivity : AppCompatActivity(){
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var  authListener: AuthStateListener
    var userId = ""
    override fun onDestroy() {
        super.onDestroy()
        Firebase.auth.removeAuthStateListener(authListener)
    }

    override fun onPause() {
        super.onPause()
        Firebase.auth.removeAuthStateListener(authListener)
        viewModel.updateLoggedData(binding.loggedWeight.text.toString().toInt(), binding.hour.text.toString().toInt(), binding.min.text.toString().toInt(), binding.numOfPeopleInTheGym.text.toString().toInt())
    }

    override fun onResume() {
        super.onResume()
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            userId = firebaseAuth.currentUser?.uid.toString()

        }
        binding.loggedWeight.setText(viewModel.loggedWeight.toString())
        binding.min.setText(viewModel.loggedMins.toString())
        binding.hour.setText(viewModel.loggedHour.toString())
        binding.numOfPeopleInTheGym.setText(viewModel.numOfPeople.toString())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //View model
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Check users registration status
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            userId = firebaseAuth.currentUser?.uid.toString()
            if (user != null) {
                // User is signed in
                Log.d("vewfwe", "user already exists")

            } else {
                val i = Intent(this, Signup::class.java)
                startActivity(i)
                finish()
            }
        }



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

        binding.hour.setText(hour.toString())
        binding.min.setText(minute.toString())


        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_ID_TOKEN)
            .requestEmail()
            .build()

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)


        val database = Firebase.database
        binding.logData.setOnClickListener {
            val loggedHour = binding.hour.text
            val loggedMinute = binding.min.text
            val loggedWeight = binding.loggedWeight.text.toString()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val formattedDate = formatter.format(Calendar.getInstance().time)
            val formattedTime = "$loggedHour:$loggedMinute"

            val userLoggedData = database.getReference("/${userId}/${formattedDate}/${formattedTime}")
            userLoggedData.push().setValue(LoggedData(numOfPeopleInGym, formattedTime, loggedWeight.toInt()))
            Toast.makeText(applicationContext, "Data logged", Toast.LENGTH_SHORT).show()

        }

        binding.logOut.setOnClickListener {
            Firebase.auth.signOut()
            Auth.GoogleSignInApi.signOut(googleSignInClient.asGoogleApiClient());
            GoogleSignIn.getClient(
                applicationContext,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            ).signOut()
            val i = Intent(this, Signup::class.java)
            startActivity(i)
            finish()

        }

    }

    override fun onBackPressed() {
//        finish()
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }
}