package com.example.gymtrovert

import android.content.ContentValues.TAG
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.gymtrovert.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.util.*

//data class LoggedData(
//    val numOfGymGoers: Int,
//    val loggedTime: String,
//    val loggedWeight: String
//)
class MainActivity : AppCompatActivity(){
//    private lateinit var viewModel: MainActivityViewModel
//    private lateinit var binding: ActivityMainBinding
//    lateinit var googleSignInClient: GoogleSignInClient
//    private lateinit var  authListener: AuthStateListener
//    var userId = ""

//    override fun onPause() {
//        super.onPause()
//
//        viewModel.updateLoggedData(binding.loggedWeight.text.toString(), binding.hour.text.toString(), binding.min.text.toString(), binding.numOfPeopleInTheGym.text.toString().toInt())
//    }

//    override fun onResume() {
//        super.onResume()
//        //if statement here because without it, all the values will be reset to 0
//        if(binding.min.text.isEmpty() && binding.hour.text.isEmpty()&& binding.loggedWeight.text.isEmpty()){
//            binding.loggedWeight.setText(viewModel.loggedWeight.toString())
//            binding.min.setText(viewModel.loggedMins.toString())
//            binding.hour.setText(viewModel.loggedHour.toString())
//            binding.numOfPeopleInTheGym.setText(viewModel.numOfPeople.toString())
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)

//     = this.findNavController(R.id.nav_host_fragment)

    val navController = supportFragmentManager
        .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)

    navView.setupWithNavController(navController.navController)

//        var numOfPeopleInGym = 0
//
//        //View model
//        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        //Check users registration status
//        val storedData = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
//        userId = storedData.getString("id", "").toString()
//
//        if(userId.isEmpty()){
//            val i = Intent(this, Signup::class.java)
//            startActivity(i)
//        }
//
//        // Launch a coroutine on the IO dispatcher to perform a background task
//        CoroutineScope(Dispatchers.IO).launch {
//            var post: String = ""
//            val postReference = Firebase.database.reference.child("/${userId}/username")
//            // Perform the background task here
//            val postListener = object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    // Get Post object and use the values to update the UI
//                     post = dataSnapshot.getValue() as String
//                    binding.welcomeText.setText("Let's Crush This Workout, $post 🤓💪!")
//
//                    Log.d(TAG, post.toString())
//                    // ...
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    // Getting Post failed, log a message
//                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
//                }
//            }
//            postReference.addValueEventListener(postListener)
//            // Switch to the main thread to update the UI
//            withContext(Dispatchers.Main) {
//                // Update the UI with the result of the background task here
//            }
//        }
//
//
//        binding.increaseNumOfPeople.setOnClickListener {
//            numOfPeopleInGym++
//            binding.numOfPeopleInTheGym.text = numOfPeopleInGym.toString()
//
//            if(numOfPeopleInGym == 1)
//                binding.peopleText.text = "person"
//            else
//                binding.peopleText.text = "people"
//
//            if(numOfPeopleInGym > 10){
//                Toast.makeText(applicationContext, "Geez, I feel bad for you :(", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        binding.reduceNumOfPeople.setOnClickListener {
//            if(numOfPeopleInGym > 0){
//                numOfPeopleInGym--
//            }
//            binding.numOfPeopleInTheGym.text = numOfPeopleInGym.toString()
//
//            if(numOfPeopleInGym == 1)
//                binding.peopleText.text = "person"
//            else
//                binding.peopleText.text = "people"
//        }
//
//        val hourFormatter = SimpleDateFormat("HH", Locale.US)
//        val hour = hourFormatter.format(Calendar.getInstance().time)
//
//        val minFormatter = SimpleDateFormat("mm", Locale.US)
//        val minute = minFormatter.format(Calendar.getInstance().time)
//
//        val meridianFormatter = SimpleDateFormat("a", Locale.US)
//        val pmOrAm = meridianFormatter.format(Calendar.getInstance().time)
//
//        binding.hour.setText(hour.toString())
//        binding.min.setText(minute.toString())
//        binding.timePMorAM.setText(pmOrAm.toString())
//        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(BuildConfig.FIREBASE_ID_TOKEN)
//            .requestEmail()
//            .build()
//
//        // Initialize sign in client
//        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
//
//
//        val database = Firebase.database
//        binding.logData.setOnClickListener {
//            val loggedHour = binding.hour.text
//            val loggedMinute = binding.min.text
//            val loggedMeridian = binding.timePMorAM.text
//            var loggedWeight =  binding.loggedWeight.text.toString()
//            if(loggedWeight == ""){
//                loggedWeight = "0"
//            }
//
//            if(loggedHour.toString() == ""){
//                Toast.makeText(applicationContext, "Hour can not be 0", Toast.LENGTH_SHORT).show()
//            }else {
//                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
//                val formattedDate = formatter.format(Calendar.getInstance().time)
//                val formattedTime = "$loggedHour:$loggedMinute $loggedMeridian"
//
//                val userLoggedData = database.getReference("/${userId}/${formattedDate}/${formattedTime}")
//                userLoggedData.push().setValue(LoggedData(numOfPeopleInGym, formattedTime, loggedWeight))
//                Toast.makeText(applicationContext, "Data logged", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        binding.logOut.setOnClickListener {
//            Firebase.auth.signOut()
//            Auth.GoogleSignInApi.signOut(googleSignInClient.asGoogleApiClient());
//            GoogleSignIn.getClient(
//                applicationContext,
//                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
//            ).signOut()
//
//            val edit = getSharedPreferences("sharedPreferences", MODE_PRIVATE).edit()
//            edit.remove("id")
//            edit.apply()
//
//
//            val i = Intent(this, Signup::class.java)
//            startActivity(i)
//            finish()
//
//        }

    }

//    override fun onBackPressed() {
//        finish()
//        val a = Intent(Intent.ACTION_MAIN)
//        a.addCategory(Intent.CATEGORY_HOME)
//        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(a)
//    }
}