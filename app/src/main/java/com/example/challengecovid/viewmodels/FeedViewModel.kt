package com.example.challengecovid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challengecovid.model.UserChallenge
import com.example.challengecovid.repository.ChallengeRepository
import com.example.challengecovid.repository.UserRepository
import kotlinx.coroutines.launch

class FeedViewModel (private val challengeRepository: ChallengeRepository, private val userRepository: UserRepository) : ViewModel() {

    val publicChallenges: LiveData<List<UserChallenge>> = challengeRepository.getPublicUserChallenges()

    fun acceptPublicChallenge (challenge: UserChallenge, userId: String) = viewModelScope.launch {
        userRepository.addActiveChallenge(challenge, userId)
    }

    /*
    fun getPublicChallenges(): LiveData<LinkedHashMap<UserChallenge, User>> = liveData(Dispatchers.IO) {
        while (true) {
            val publicUserChallenges = challengeRepository.getPublicUserChallenges().value ?: return@liveData
            val users = getUsersAndChallenges(publicUserChallenges)

            if (publicUserChallenges.size != users.size) return@liveData

            val map = publicUserChallenges.zip(users).toMap()
            emit(map as LinkedHashMap<UserChallenge, User>)

            delay(5000)
        }

    }
    private suspend fun getUsersAndChallenges(challenges: List<UserChallenge>): List<User> {
        val users = mutableListOf<User>()
        for (challenge in challenges) {
            val user = userRepository.getUser(challenge.creatorId)
            user?.let { users.add(it) }
        }
        return users
    }

     */

    /*
    fun updateCategory(category: ChallengeCategory) = viewModelScope.launch(Dispatchers.IO) {
        categoryRepository.updateCategory(category)
    }*/
}