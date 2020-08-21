package com.example.challengecovid.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.challengecovid.database.ChallengeDatabase
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.repository.ChallengeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChallengeViewModel2(application: Application) : AndroidViewModel(application){
    private val repository: ChallengeRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allChallenges: LiveData<List<Challenge>>

    init {
        val challengeDao = ChallengeDatabase.getInstance(application)
        repository = ChallengeRepository(challengeDao)
        allChallenges = repository.allChallenges
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(challenge: Challenge) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(challenge)
    }
    fun delete(challenge: Challenge) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(challenge)
    }

}