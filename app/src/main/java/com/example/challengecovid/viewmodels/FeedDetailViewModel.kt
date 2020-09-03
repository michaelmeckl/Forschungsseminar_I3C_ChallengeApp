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
        return@withContext challengeRepository.getChallengeParticipants(challengeId)
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
        /*
        //check if the user has this challenge already accepted and if so return
        val allChallenges = userRepository.getAllChallengesForUserOnce(userId, hidden = false)?.toSet()
        if (allChallenges != null && allChallenges.contains(challenge)) {
            // switch to Main Thread to show toast
            withContext(Dispatchers.Main) {
                Toast.makeText(App.instance, "Du hast diese Challenge schon angenommen!", Toast.LENGTH_SHORT).show()
            }
            return@launch
        }
        */

        // add this challenge to the users active challenges
        userRepository.addActiveChallenge(challenge, user.userId)
        // add the user to this challenge's participants
        challengeRepository.addParticipantToChallenge(challenge.challengeId, user)
    }
}