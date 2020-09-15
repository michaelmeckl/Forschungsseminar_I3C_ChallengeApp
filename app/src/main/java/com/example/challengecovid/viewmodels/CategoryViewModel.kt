package com.example.challengecovid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.challengecovid.model.ChallengeCategory
import com.example.challengecovid.repository.CategoryRepository

class CategoryViewModel (private val categoryRepository: CategoryRepository) : ViewModel() {

    val allCategories: LiveData<List<ChallengeCategory>> = categoryRepository.getAllCategories()

    /*
    private var currentCategory = MutableLiveData<ChallengeCategory>()

    fun updateCategory(category: ChallengeCategory) = viewModelScope.launch(Dispatchers.IO) {
        categoryRepository.updateCategory(category)
    }*/
}