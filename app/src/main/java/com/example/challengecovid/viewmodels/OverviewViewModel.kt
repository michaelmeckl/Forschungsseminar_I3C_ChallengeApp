package com.example.challengecovid.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.challengecovid.Constants
import com.example.challengecovid.R
import com.example.challengecovid.adapter.OverviewAdapter
import com.example.challengecovid.model.BaseChallenge
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.UserChallenge
import com.example.challengecovid.repository.ChallengeRepository
import com.example.challengecovid.repository.UserRepository
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * A ViewModel is designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results or performing database operations can continue through
 * configuration changes and deliver results after the new Fragment or Activity is available.
 */
class OverviewViewModel(
    private val challengeRepository: ChallengeRepository,
    private val userRepository: UserRepository,
    application: Application
) : AndroidViewModel(application) {

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

    private var currentUserId: String = ""
    lateinit var allChallenges: LiveData<List<BaseChallenge>>

    init {
        val sharedPrefs = application.getSharedPreferences(
            Constants.SHARED_PREFS_NAME,
            AppCompatActivity.MODE_PRIVATE
        )
        currentUserId = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""

        //Toast.makeText(application, "UserID: $currentUserId", Toast.LENGTH_SHORT).show()
        Timber.d(currentUserId)

        if (currentUserId === "") {
            Toast.makeText(
                application,
                application.getString(R.string.wrong_user_id_error),
                Toast.LENGTH_LONG
            ).show()
        } else {
            allChallenges = userRepository.getAllChallengesForUser(currentUserId)
        }
    }

    /**
     * Request a toast by setting this value to true.
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private var _showDailyChallengeEvent = MutableLiveData<Boolean?>()

    /**
     * If this is true, immediately `show()` a toast and call `doneShowingSnackbar()`.
     */
    val showDailyChallengeEvent: LiveData<Boolean?>
        get() = _showDailyChallengeEvent

    /**
     * Call this immediately after calling `show()` on a toast.
     * It will clear the toast request, so if the user rotates their phone it won't show a duplicate
     * toast.
     */
    fun doneShowingSnackbar() {
        _showDailyChallengeEvent.value = null
    }

    /**
     * Add a new challenge to the database.
    fun addNewChallenge(userChallenge: UserChallenge) {
    //launch on the main thread because the result affects the UI
    uiScope.launch {
    // insert the new challenge on a separate I/O thread that is optimized for room interaction
    // to avoid blocking the main / UI thread
    withContext(Dispatchers.IO) {
    challengeRepository.saveUserChallenge(userChallenge)
    }
    }
     */

    fun addNewChallenge(userChallenge: UserChallenge) {
        challengeRepository.saveNewUserChallenge(userChallenge)
        userRepository.addActiveChallenge(userChallenge, currentUserId)
    }

    private fun initializeChallenge(challengeID: String) {
        uiScope.launch {
            currentChallenge.value = challengeRepository.getUserChallenge(challengeID)
        }
    }

    fun updateChallenge(userChallenge: UserChallenge) {
        challengeRepository.updateUserChallenge(userChallenge)
        userRepository.updateActiveChallenge(userChallenge, currentUserId)
    }

    fun updatePublicStatus(challengeId: String, status: Boolean) = viewModelScope.launch {
        challengeRepository.updatePublicStatus(challengeId, status)
    }

    /*
    fun removeChallenge(challenge: BaseChallenge) {
        if(challenge.type == ChallengeType.USER_CHALLENGE) {
            //TODO: so nicht (Cast von BaseChallenge nicht möglich!):
            //challengeRepository.deleteUserChallenge(challenge as? UserChallenge)
            userRepository.removeActiveChallenge(challenge, currentUserId)
        }

        if(challenge.type == ChallengeType.SYSTEM_CHALLENGE) {
            userRepository.removeActiveChallenge(challenge, currentUserId)
        }
    }*/

    fun removeChallenge(challengeId: String) {
        userRepository.removeActiveChallenge(challengeId, currentUserId)
    }

    fun hideChallenge(challenge: Challenge) {
        userRepository.hideActiveChallenge(challenge, currentUserId)
    }

    //TODO: use delete instead when completed?
    fun setChallengeCompleted(challenge: BaseChallenge) = uiScope.launch {
        withContext(Dispatchers.IO) {
            challengeRepository.updateCompletionStatus(
                challenge.challengeId,
                challenge.type,
                completed = true
            )
            challenge.completed = true
            userRepository.addActiveChallenge(challenge, currentUserId)
        }
    }

    fun setAllChallengesToNotCompleted(challengeList: List<BaseChallenge>) = uiScope.launch {
        withContext(Dispatchers.IO) {
            for (challenge in challengeList) {
//            val challengeSnapshot = userChallengeCollection.document(challenge.challengeId).get().await()
                if (challenge.completed) {
                    challenge.completed = false
                    userRepository.addActiveChallenge(challenge, currentUserId)
                }
            }
        }


    }

    fun getRandomDailyChallenge(oldDailyChallenge: String?) = uiScope.launch {
        withContext(Dispatchers.IO) {
            val randomChallenge =
                challengeRepository.getRandomChallenge(oldDailyChallenge) ?: return@withContext
            userRepository.updateActiveChallenge(randomChallenge, currentUserId)
        }
        _showDailyChallengeEvent.value = true
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