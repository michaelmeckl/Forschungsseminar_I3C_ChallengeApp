package com.example.challengecovid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.challengecovid.model.UserChallenge
import com.example.challengecovid.repository.ChallengeRepository
import com.example.challengecovid.repository.UserRepository

class FeedViewModel (private val challengeRepository: ChallengeRepository, private val userRepository: UserRepository) : ViewModel() {

    val publicChallenges: LiveData<List<UserChallenge>> = challengeRepository.getPublicUserChallenges()
}