package com.example.gymtrovert

import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
//    private var loggedWeight
    var loggedWeight = 0
    var loggedHour = 0
    var loggedMins = 0
    var numOfPeople = 0

    fun updateLoggedData(newLoggedWeight: Int, newLoggedHour: Int, newLoggedMins: Int, newNumOfPeople: Int){
        loggedWeight = newLoggedWeight
        loggedHour = newLoggedHour
        loggedMins = newLoggedMins
        numOfPeople = newNumOfPeople
    }
}