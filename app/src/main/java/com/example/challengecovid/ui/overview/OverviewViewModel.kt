package com.example.challengecovid.ui.overview

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.challengecovid.database.ChallengeDao
import com.example.challengecovid.database.Challenge
import kotlinx.coroutines.*
import java.util.*

class OverviewViewModel (dataSource: ChallengeDao,
                         application: Application
) : ViewModel() {

    /**
     * Hold a reference to the Database via the Dao.
     */
    private val databaseRef = dataSource

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

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
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //val challenges: MutableLiveData<List<Challenge>> by lazy { MutableLiveData<List<Challenge>>() }
    val challenges = databaseRef.getAllChallenges()

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

        //TODO: test if snackbar and updating db works!
        //update()
    }

    /**
     * Call this immediately after calling `show()` on a toast.
     * It will clear the toast request, so if the user rotates their phone it won't show a duplicate
     * toast.
     */
    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = null
    }

    private fun initializeChallenge(challengeID: Int) {
        uiScope.launch {
            challenge.value = getChallengeFromDatabase(challengeID)
        }
    }

    private suspend fun getChallengeFromDatabase(challengeID: Int): Challenge? {
        return withContext(Dispatchers.IO) {
            val challenge = databaseRef.get(challengeID)
            if (challenge.duration <= (Date().time - challenge.startTime)) {
                // Duration Time is over -> challenge is outdated!
                return@withContext null
            }
            challenge
        }
    }

    private suspend fun insert(challenge: Challenge) {
        withContext(Dispatchers.IO) {
            databaseRef.insert(challenge)
        }
        _showSnackbarEvent.value = true
    }

    private suspend fun update(challenge: Challenge) {
        withContext(Dispatchers.IO) {
            databaseRef.update(challenge)
        }
        _showSnackbarEvent.value = true
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            databaseRef.clear()
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