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
import com.example.gymtrovert.R
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

data class LoggedDate(
    val key: String = "",
    val data: List<Map<String, LoggedData>> = emptyList()
)
data class LoggedData(
    val loggedTime: String = "",
    val loggedWeight: String = "",
    val numOfGymGoers: Int = 0
)
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
                val currentDate = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                val formattedCurrentDate = currentDate.format(Calendar.getInstance().time)
                selectedDate = formattedCurrentDate
            }

            Log.d(TAG, selectedDate)
        }

        val calendar = binding.calendarView
        calendar.setOnDateChangeListener( object : CalendarView.OnDateChangeListener {
            override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
                val selectedDate = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }.time

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate)
                Log.d(TAG, "Selected date: $formattedDate")
            }
        });

        // Inflate the layout for this fragment
        val postReference = Firebase.database.reference.child("/$userId/gymLogs")

        var myDataset = mutableListOf<LoggedDate>()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                myDataset.clear()

                // Get Post object and use the values to update the UI
                val dataText=  dataSnapshot.getValue<LoggedDate>()

//                Log.d("TEST", dataText[0].loggedWeight)
//                    Log.d("AAAAA2", dataText?.gymLogs.toString())
                for(it in dataSnapshot.children){
                    Log.d("KEU", it.key.toString())
                    Log.d("VAL", it.value.toString())
                      val test = LoggedDate(
                          it.key.toString(),
                          listOf(mapOf("" to LoggedData()))
                      )

                    Log.d("AAAAAAA", test.toString())
//                    mapOf(it.getValue<LoggedDate>()?.key.toString() to it.getValue<LoggedData>()!!)
////                       it.getValue<LoggedData>()?.loggedTime.toString(),
////                       it.getValue<LoggedData>()?.loggedWeight.toString(),
////                       it.getValue<LoggedData>()?.numOfGymGoers!!.toInt(),
//                   )

//                    myDataset.add(test)
                }

//                Log.d("AA ", myDataset.toString())


//                val histories = dataSnapshot.getValue<GymLogs>()
//                val test = GymLogs(mapOf(dataSnapshot.key.toString() to LogsByTime(dataSnapshot.children. value)))

//                val histories = dataSnapshot.getValue<LogDates>()
//                histories?.times?.forEach {
//                    Log.d("pls work", it.toString())
//                    it.logInfo
//                }


//                Log.d("AAAAAAAA", histories.toString())

//                histories.logsByDate.entries{
//                    Log.d("test", history.toString())
//                    Log.d("key", history.key.toString())
//
//                     var test = GymLogs(mapOf(history.key.toString() to LogsByTime(mapOf(history.value))))
//                }
//                Log.d("AAAAAAA", post.toString())
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        postReference.addValueEventListener(postListener)

        return view
    }
}