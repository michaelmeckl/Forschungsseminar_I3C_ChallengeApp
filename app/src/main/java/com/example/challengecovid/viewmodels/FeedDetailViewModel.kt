package com.example.challengecovid.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challengecovid.model.BaseChallenge
import com.example.challengecovid.model.User
import com.example.challengecovid.model.UserChallenge
import com.example.challengecovid.repository.ChallengeRepository
import com.example.challengecovid.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeedDetailViewModel (private val challengeRepository: ChallengeRepository, private val userRepository: UserRepository) : ViewModel() {

    suspend fun getParticipantsForChallenge(challengeId: String): List<User>? = withContext(Dispatchers.IO) {
        return@withContext userRepository.getChallengeParticipants(challengeId)
    }

    suspend fun getUsersChallenges(userId: String): List<BaseChallenge>? = withContext(Dispatchers.IO) {
        return@withContext userRepository.getAllChallengesForUserOnce(userId, hidden = false)
    }

    suspend fun getChallenge(challengeId: String): UserChallenge? = withContext(Dispatchers.IO) {
        return@withContext challengeRepository.getUserChallenge(challengeId)
    }

    suspend fun getCurrentUser(userId: String): User? = withContext(Dispatchers.IO) {
        return@withContext userRepository.getUserOnce(userId)
    }

    fun acceptPublicChallenge (challenge: UserChallenge, user: User) = viewModelScope.launch(Dispatchers.IO) {
        // add this challenge to the users active challenges
        userRepository.addActiveChallenge(challenge, user.userId)
        // add the user to this challenge's participants
        // challengeRepository.addParticipantToChallenge(challenge.challengeId, user)   // not necessary anymore
    }
}