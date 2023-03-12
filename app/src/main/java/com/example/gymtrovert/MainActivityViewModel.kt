package com.example.gymtrovert

import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
//    private var loggedWeight
    var loggedWeight = ""
    var loggedHour = ""
    var loggedMins = ""
    var numOfPeople = 0

    fun updateLoggedData(newLoggedWeight: String, newLoggedHour: String, newLoggedMins: String, newNumOfPeople: Int){
        loggedWeight = newLoggedWeight
        loggedHour = newLoggedHour
        loggedMins = newLoggedMins
        numOfPeople = newNumOfPeople
    }


}