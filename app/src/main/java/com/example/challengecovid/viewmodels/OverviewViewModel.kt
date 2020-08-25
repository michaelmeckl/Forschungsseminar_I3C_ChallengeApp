package com.example.challengecovid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.repository.ChallengeRepository
import com.example.challengecovid.model.UserChallenge
import kotlinx.coroutines.*

/**
 * A ViewModel is designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results or performing database operations can continue through
 * configuration changes and deliver results after the new Fragment or Activity is available.
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
class OverviewViewModel (private val challengeRepository: ChallengeRepository) : ViewModel() {

    /**
     * This is the job for all coroutines started by this ViewModel.
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = SupervisorJob()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var currentChallenge = MutableLiveData<UserChallenge>()

    val allChallenges: LiveData<List<UserChallenge>> = challengeRepository.getAllUserChallenges()

    /**
     * Request a toast by setting this value to true.
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private var _showSnackbarEvent = MutableLiveData<Boolean?>()

    /**
     * If this is true, immediately `show()` a toast and call `doneShowingSnackbar()`.
     */
    val showSnackBarEvent: LiveData<Boolean?>
        get() = _showSnackbarEvent

    /**
     * Call this immediately after calling `show()` on a toast.
     * It will clear the toast request, so if the user rotates their phone it won't show a duplicate
     * toast.
     */
    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = null
    }

    /**
     * Add a new challenge to the database.
     */
    fun insertNewChallenge(userChallenge: UserChallenge) {
        //launch on the main thread because the result affects the UI
        uiScope.launch {
            // insert the new challenge on a separate I/O thread that is optimized for room interaction
            // to avoid blocking the main / UI thread
            withContext(Dispatchers.IO) {
                challengeRepository.insertUserChallenge(userChallenge)
            }
            _showSnackbarEvent.value = true
        }
    }

    private fun initializeChallenge(challengeID: String) {
        uiScope.launch {
            currentChallenge.value = getUserChallengeFromDatabase(challengeID)
        }
    }

    private suspend fun getUserChallengeFromDatabase(challengeID: String): UserChallenge? {
        return withContext(Dispatchers.IO) {
            challengeRepository.getUserChallenge(challengeID).value
        }
    }

    fun updateChallenge(userChallenge: UserChallenge) = uiScope.launch {
        withContext(Dispatchers.IO) {
            challengeRepository.updateUserChallenge(userChallenge)
        }
        _showSnackbarEvent.value = true
    }

    fun removeChallenge(userChallenge: UserChallenge) = uiScope.launch {
        withContext(Dispatchers.IO) {
            challengeRepository.deleteUserChallenge(userChallenge)
        }
        _showSnackbarEvent.value = true
    }

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines; otherwise we end up with processes that have nowhere to return
     * to using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}