package com.example.challengecovid.ui.overview

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.challengecovid.database.ChallengeDao

/**
 * Factory for constructing the OverviewViewmodel with parameters.
 */
class OverviewViewmodelFactory(private val dataSource: ChallengeDao,
                               private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OverviewViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}