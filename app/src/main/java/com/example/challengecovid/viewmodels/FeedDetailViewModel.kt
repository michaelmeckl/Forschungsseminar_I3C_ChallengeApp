package com.example.challengecovid.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challengecovid.model.User
import com.example.challengecovid.repository.ChallengeRepository
import com.example.challengecovid.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeedDetailViewModel (private val challengeRepository: ChallengeRepository, private val userRepository: UserRepository) : ViewModel() {

    suspend fun getParticipantsForChallenge(challengeId: String): List<User>? = withContext(Dispatchers.IO) {
        return@withContext challengeRepository.getChallengeParticipants(challengeId)
    }

    fun acceptPublicChallenge (challengeId: String, userId: String) = viewModelScope.launch(Dispatchers.IO) {
        //TODO: ist das die richtige collection?
        val challenge = challengeRepository.getUserChallenge(challengeId) ?: return@launch
        userRepository.addActiveChallenge(challenge, userId)

        val user = userRepository.getUserOnce(userId) ?: return@launch
        challengeRepository.addParticipantToChallenge(challengeId, user)
    }
}