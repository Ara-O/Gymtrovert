package com.example.gymtrovert

import android.content.ContentValues.TAG
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.gymtrovert.databinding.ActivityMainBinding
import com.example.gymtrovert.databinding.FragmentMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.*
import java.util.*


data class LoggedDataWithoutKey(
    val loggedTime: String = "",
    val loggedWeight: String = "",
    val numOfGymGoers: Int = 0
)
class MainFragment : Fragment() {
    private lateinit var viewModel: MainActivityViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
//    private lateinit var  authListener: AuthStateListener
    var userId = ""

    override fun onPause() {
        super.onPause()

        viewModel.updateLoggedData(binding.loggedWeight.text.toString(), binding.hour.text.toString(), binding.min.text.toString(), binding.numOfPeopleInTheGym.text.toString().toInt())
    }

    override fun onResume() {
        super.onResume()
        //if statement here because without it, all the values will be reset to 0
        if(binding.min.text.isEmpty() && binding.hour.text.isEmpty()&& binding.loggedWeight.text.isEmpty()){
            binding.loggedWeight.setText(viewModel.loggedWeight.toString())
            binding.min.setText(viewModel.loggedMins.toString())
            binding.hour.setText(viewModel.loggedHour.toString())
            binding.numOfPeopleInTheGym.setText(viewModel.numOfPeople.toString())
        }
    }
//

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root

        var numOfPeopleInGym = 0
        var username = ""
        //View model
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        //Check users registration status
        val storedData = activity?.getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        if (storedData != null) {
            userId = storedData.getString("id", "").toString()
            username = storedData.getString("username", "").toString()
            Log.d("username", username)
        }

        if(userId.isEmpty()){
            val i = Intent(context, Signup::class.java)
            startActivity(i)
        }

        binding.welcomeText.setText("Let's Crush This Workout, $username 💪🏋️!")


        binding.increaseNumOfPeople.setOnClickListener {
            numOfPeopleInGym++
            binding.numOfPeopleInTheGym.text = numOfPeopleInGym.toString()

            if(numOfPeopleInGym == 1)
                binding.peopleText.text = "person"
            else
                binding.peopleText.text = "people"

            if(numOfPeopleInGym > 10){
                Toast.makeText(context, "Geez, I feel bad for you :(", Toast.LENGTH_SHORT).show()
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

        val hourFormatter = SimpleDateFormat("hh", Locale.US)
        val hour = hourFormatter.format(Calendar.getInstance().time)

        val minFormatter = SimpleDateFormat("mm", Locale.US)
        val minute = minFormatter.format(Calendar.getInstance().time)

        val meridianFormatter = SimpleDateFormat("a", Locale.US)
        val pmOrAm = meridianFormatter.format(Calendar.getInstance().time)

        binding.hour.setText(hour.toString())
        binding.min.setText(minute.toString())
        binding.timePMorAM.setText(pmOrAm.toString())


        val database = Firebase.database
        binding.logData.setOnClickListener {
            val loggedHour = binding.hour.text
            val loggedMinute = binding.min.text
            val loggedMeridian = binding.timePMorAM.text
            var loggedWeight =  binding.loggedWeight.text.toString()
            if(loggedWeight == ""){
                loggedWeight = "0"
            }

            if(loggedHour.toString() == ""){
                Toast.makeText(context, "Hour can not be 0", Toast.LENGTH_SHORT).show()
            }else {
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val formattedDate = formatter.format(Calendar.getInstance().time)
                val formattedTime = "$loggedHour:$loggedMinute $loggedMeridian"

                val userLoggedData = database.getReference("/${userId}/gymLogs/${formattedDate}")
                userLoggedData.push().setValue(LoggedDataWithoutKey(formattedTime, loggedWeight,  numOfPeopleInGym.toInt()))
                Toast.makeText(context, "Data logged", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}