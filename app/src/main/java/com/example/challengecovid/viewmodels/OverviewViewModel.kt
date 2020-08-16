package com.example.challengecovid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengecovid.R
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.Difficulty
import com.example.challengecovid.repository.ChallengeRepository
import kotlinx.coroutines.*
import java.util.*

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
class OverviewViewModel (challengeRepository: ChallengeRepository) : ViewModel() {

    private val dataSource = challengeRepository

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

    //val challenges: MutableLiveData<List<Challenge>> by lazy { MutableLiveData<List<Challenge>>() }
    val challenges = dataSource.getAllChallenges()

    private var challenge = MutableLiveData<Challenge?>()


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


    init {
        //initializeChallenge(TODO())

        // TODO: refresh the room db with new challenges from other players?
        /*
        uiScope.launch {
            try {
                challengeRepository.refreshChallenges()
            } catch (e: IOException) {
                Timber.e(e)
            }
        }*/

        val ch = Challenge(
            title = "Geänderte Challenge",
            description = "Custom Description2",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 5f,
            iconPath = R.drawable.ic_done
        )
        uiScope.launch {
            update(ch)
        }

    }

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
    fun addNewChallenge(challenge: Challenge) {
        //launch on the main thread because the result affects the UI
        uiScope.launch {
            insert(challenge)
        }
    }

    private fun initializeChallenge(challengeID: String) {
        uiScope.launch {
            challenge.value = getChallengeFromDatabase(challengeID)
        }
    }

    private suspend fun getChallengeFromDatabase(challengeID: String): Challenge? {
        return withContext(Dispatchers.IO) {
            val challenge = dataSource.getChallenge(challengeID).value
            val difference = System.currentTimeMillis() - challenge?.createdAt!!
            /*
            if (challenge.duration <= difference) {
                // Duration Time is over -> challenge is outdated!
                return@withContext null
            }

             */
            challenge
        }
    }

    //TODO: instead fetch from the repository instead!
    fun getChallenge(challenge: Challenge): LiveData<Challenge> {
        return dataSource.getChallenge(challenge.challengeId)
    }

    private suspend fun insert(challenge: Challenge) {
        // insert the new challenge on a separate I/O thread that is optimized for room interaction
        // to avoid blocking the main / UI thread
        withContext(Dispatchers.IO) {
            dataSource.insertNewChallenge(challenge)
        }
        _showSnackbarEvent.value = true
    }

    private suspend fun update(challenge: Challenge) {
        withContext(Dispatchers.IO) {
            dataSource.updateChallenge(challenge)
        }
        _showSnackbarEvent.value = true
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            dataSource.deleteAllChallenges()
        }
        _showSnackbarEvent.value = true
    }

    /**
     * Clear the challenge infos.
     */
    fun onClear() {
        uiScope.launch {
            // Clear the database table.
            clear()

            // And clear tonight since it's no longer in the database
            challenge.value = null

            // Show a snackbar message, because it's friendly.
            _showSnackbarEvent.value = true
        }
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