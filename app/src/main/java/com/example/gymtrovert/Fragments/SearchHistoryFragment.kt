package com.example.gymtrovert.Fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gymtrovert.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SearchHistoryFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var userId = ""
        val storedData = activity?.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        if (storedData != null) {
            userId = storedData.getString("id", "").toString()
        }
        // Inflate the layout for this fragment
        val postReference = Firebase.database.reference.child("/$userId")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
               val post = dataSnapshot.getValue()
                Log.d("AAAAAAA", post.toString())
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        postReference.addValueEventListener(postListener)

        return inflater.inflate(R.layout.fragment_search_history, container, false)
    }
}