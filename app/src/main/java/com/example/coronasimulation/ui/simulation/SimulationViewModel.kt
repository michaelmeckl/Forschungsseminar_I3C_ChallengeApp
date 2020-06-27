package com.example.coronasimulation.ui.simulation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SimulationViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is simulation Fragment"
    }
    val text: LiveData<String> = _text
}