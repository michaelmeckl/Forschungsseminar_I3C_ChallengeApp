package com.example.challengecovid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.challengecovid.model.UserChallenge
import com.example.challengecovid.repository.ChallengeRepository

class FeedViewModel (private val challengeRepository: ChallengeRepository) : ViewModel() {

    val publicChallenges: LiveData<List<UserChallenge>> = challengeRepository.getPublicUserChallenges()

    /*
    fun updateCategory(category: ChallengeCategory) = viewModelScope.launch(Dispatchers.IO) {
        categoryRepository.updateCategory(category)
    }*/
}