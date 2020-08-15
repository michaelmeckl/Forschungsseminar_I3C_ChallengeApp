package com.example.challengecovid.viewmodels

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.challengecovid.database.ChallengeDao
import com.example.challengecovid.repository.ChallengeRepository

/**
 * Factory for constructing the OverviewViewmodel with parameters.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: ChallengeRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
            return OverviewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel!")
    }
}