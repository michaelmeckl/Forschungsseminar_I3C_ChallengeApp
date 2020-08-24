package com.example.challengecovid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challengecovid.database.repository.CategoryRepository
import com.example.challengecovid.model.ChallengeCategory
import kotlinx.coroutines.*

class CategoryViewModel (private val categoryRepository: CategoryRepository) : ViewModel() {

    val allCategories: LiveData<List<ChallengeCategory>> = categoryRepository.getAllChallengeCategories()

    private var currentCategory = MutableLiveData<ChallengeCategory>()

    suspend fun update(category: ChallengeCategory) = viewModelScope.launch(Dispatchers.IO) {
        categoryRepository.updateChallengeCategory(category)
    }

}