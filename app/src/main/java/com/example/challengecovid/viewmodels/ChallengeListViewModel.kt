package com.example.challengecovid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challengecovid.database.repository.ChallengeRepository
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.UserChallenge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChallengeListViewModel(private val challengeRepository: ChallengeRepository) : ViewModel() {

    val allChallenges: LiveData<List<UserChallenge>> = challengeRepository.getAllUserChallenges()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(userChallenge: UserChallenge) = viewModelScope.launch(Dispatchers.IO) {
        challengeRepository.insertUserChallenge(userChallenge)
    }

    fun delete(userChallenge: UserChallenge) = viewModelScope.launch(Dispatchers.IO) {
        challengeRepository.deleteUserChallenge(userChallenge)
    }

}