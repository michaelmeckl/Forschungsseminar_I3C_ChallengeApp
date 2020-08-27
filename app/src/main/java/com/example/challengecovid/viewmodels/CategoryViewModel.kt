package com.example.challengecovid.viewmodels

import androidx.lifecycle.*
import com.example.challengecovid.repository.CategoryRepository
import com.example.challengecovid.model.ChallengeCategory
import kotlinx.coroutines.*

class CategoryViewModel (private val categoryRepository: CategoryRepository) : ViewModel() {

    val allCategories: LiveData<List<ChallengeCategory>> = categoryRepository.getAllCategories()

    private var currentCategory = MutableLiveData<ChallengeCategory>()

    fun updateCategory(category: ChallengeCategory) = viewModelScope.launch(Dispatchers.IO) {
        categoryRepository.updateCategory(category)
    }
}