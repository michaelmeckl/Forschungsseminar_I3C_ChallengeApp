package com.example.coronasimulation.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coronasimulation.model.CoronaStatistics

class NewsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is news Fragment"
    }
    val text: LiveData<String> = _text

    val statistics: MutableLiveData<CoronaStatistics> by lazy { MutableLiveData<CoronaStatistics>() }
}