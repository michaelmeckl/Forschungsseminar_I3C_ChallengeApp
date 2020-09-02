package com.example.challengecovid.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.challengecovid.model.BaseChallenge
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.repository.CategoryRepository
import com.example.challengecovid.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryDetailViewModel(private val categoryRepository: CategoryRepository, private val userRepository: UserRepository, application: Application) :
    AndroidViewModel(application) {

    val challengesForCategory = MutableLiveData<List<Challenge>>()

    suspend fun getChallengesForCategory(categoryId: String): List<Challenge>? = withContext(Dispatchers.IO) {
        return@withContext categoryRepository.fetchChallengesForCategory(categoryId)
    }

    suspend fun getActiveAndHiddenChallenges(userId: String): List<BaseChallenge>? = withContext(Dispatchers.IO) {
        return@withContext userRepository.getActiveAndHiddenChallengesForUser(userId)
    }

    fun addToActiveChallenges(categoryId: String, challenge: Challenge, userId: String) {
        viewModelScope.launch {
            userRepository.addActiveChallenge(challenge, userId)
            //TODO nicht so, da ja sonst jedesmal verändert bei jedem nutzer
            //categoryRepository.changeChallengeActiveStatus(categoryId, challenge, status = true)
        }
    }

}