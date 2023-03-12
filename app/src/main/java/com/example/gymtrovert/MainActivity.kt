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


class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//     = this.findNavController(R.id.nav_host_fragment)

    val navController = supportFragmentManager
        .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)

    navView.setupWithNavController(navController.navController)


    }

//    override fun onBackPressed() {
//        finish()
//        val a = Intent(Intent.ACTION_MAIN)
//        a.addCategory(Intent.CATEGORY_HOME)
//        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(a)
//    }
}