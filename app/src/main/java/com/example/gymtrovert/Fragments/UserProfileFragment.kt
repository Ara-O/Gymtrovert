package com.example.gymtrovert.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gymtrovert.BuildConfig
import com.example.gymtrovert.R
import com.example.gymtrovert.Signup
import com.example.gymtrovert.databinding.FragmentUserProfileBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class UserProfileFragment : Fragment() {
    lateinit var googleSignInClient: GoogleSignInClient
    private var _binding: FragmentUserProfileBinding? = null
    private lateinit var database: DatabaseReference

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        var username = ""
        var userId = ""
        val storedData = activity?.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        if (storedData != null) {
                username = storedData.getString("username", "").toString()
                userId = storedData.getString("id", "").toString()
        }

        binding.usernameTextInput.setText(username)
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_ID_TOKEN)
            .requestEmail()
            .build()

        // Initialize sign in client
        googleSignInClient = activity?.let { GoogleSignIn.getClient(it, googleSignInOptions) }!!

        database = Firebase.database.reference

        binding.updateButton.setOnClickListener {
            Toast.makeText(context, "Username Updated", Toast.LENGTH_SHORT).show()
            database.child(userId).child("/username").setValue(binding.usernameTextInput.text.toString())

            val sharedPreferences = requireActivity().getSharedPreferences("sharedPreferences",
                AppCompatActivity.MODE_PRIVATE
            )
            val firebasePref = sharedPreferences.edit()
            firebasePref.putString("username", binding.usernameTextInput.text.toString())
            firebasePref.apply()

        }

        binding.logOutButton.setOnClickListener {
            Toast.makeText(requireContext(), "Logging out", Toast.LENGTH_SHORT).show()
            Firebase.auth.signOut()
            Auth.GoogleSignInApi.signOut(googleSignInClient.asGoogleApiClient());
                    context?.let { it1 ->
                        GoogleSignIn.getClient(
                            it1,
                            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                        ).signOut()
                    }

                    val edit = activity?.getSharedPreferences("sharedPreferences",
                        Context.MODE_PRIVATE
                    )?.edit()
                    edit?.remove("id")
                    edit?.remove("username")
                    edit?.apply()

                    val i = Intent(context, Signup::class.java)
                    startActivity(i)
                }

                val view = binding.root
                return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}