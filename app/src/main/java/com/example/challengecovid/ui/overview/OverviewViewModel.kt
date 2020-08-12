package com.example.challengecovid.ui.overview

import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengecovid.R
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.CoronaStatistics

class OverviewViewModel : ViewModel() {

    val challenges: MutableLiveData<List<Challenge>> by lazy { MutableLiveData<List<Challenge>>() }

    init {
        //challenges.value = TODO()
    }

    //TODO: get from db

}