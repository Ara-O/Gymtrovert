package com.example.gymtrovert.Fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import com.example.gymtrovert.LoggedData
import com.example.gymtrovert.R
import com.example.gymtrovert.adapter.ItemAdapter
import com.example.gymtrovert.databinding.FragmentSearchHistoryBinding
import com.example.gymtrovert.databinding.SearchHistoryItemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.selects.select
import java.text.SimpleDateFormat
import java.util.*


//
//data class DateData(
//    val data: Map<String, Map<String, LoggedData>> = emptyMap()
//)


class SearchHistoryFragment : Fragment() {
    private var _binding: FragmentSearchHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchHistoryBinding.inflate(inflater, container, false)
        val view = binding.root

        var userId = ""
        val storedData = activity?.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        if (storedData != null) {
            userId = storedData.getString("id", "").toString()
        }

        var selectedDate = ""

        binding.seeHistoryButton.setOnClickListener {
            if(selectedDate.isEmpty()){
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedCurrentDate = currentDate.format(Calendar.getInstance().time)
                selectedDate = formattedCurrentDate
            }


            Log.d(TAG, selectedDate)

            // Inflate the layout for this fragment
            val postReference = Firebase.database.reference.child("/$userId/gymLogs/$selectedDate")

            var myDataset = mutableListOf<LoggedData>()
            postReference.get().addOnSuccessListener {
                myDataset.clear()

                    for (it in it.children) {
                        Log.d("IT", it.value.toString())
                        val data = LoggedData(it.getValue<LoggedData>()?.loggedWeight.toString(), it.getValue<LoggedData>()?.loggedTime.toString(), it.getValue<LoggedData>()?.numOfGymGoers.toString())
                        myDataset.add(data)
                    }
                val recyclerView = binding.recyclerView
                recyclerView.adapter = ItemAdapter(requireContext(), myDataset)
                recyclerView.setHasFixedSize(true)

//                Log.i("firebase", "Got value ${it.value}")
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
//            val postListener = object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    myDataset.clear()
//
//                    for (it in dataSnapshot.children) {
//                        Log.d("IT", it.value.toString())
//                        val data = LoggedData(it.getValue<LoggedData>()?.loggedWeight.toString(), it.getValue<LoggedData>()?.loggedTime.toString(), it.getValue<LoggedData>()?.numOfGymGoers.toString())
//                        myDataset.add(data)
//                    }
//
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    // Getting Post failed, log a message
//                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
//                }
//            }
//            postReference.addValueEventListener(postListener)



        }


        val calendar = binding.calendarView
        calendar.setOnDateChangeListener( object : CalendarView.OnDateChangeListener {
            override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
                val selectedDateCalendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }.time

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDateCalendar)
                selectedDate = formattedDate
//                Log.d(TAG, "Selected date: $formattedDate")
            }
        });



        return view
    }
}