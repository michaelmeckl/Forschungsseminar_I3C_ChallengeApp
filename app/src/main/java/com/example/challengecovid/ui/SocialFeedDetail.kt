package com.example.challengecovid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challengecovid.Constants
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.adapter.FeedDetailAdapter
import com.example.challengecovid.measureTimeMillis
import com.example.challengecovid.model.User
import com.example.challengecovid.model.UserChallenge
import com.example.challengecovid.viewmodels.FeedDetailViewModel
import com.example.challengecovid.viewmodels.getViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_social_feed_detail.*
import kotlinx.coroutines.*
import timber.log.Timber

class SocialFeedDetail : DialogFragment() {

    private val arguments: SocialFeedDetailArgs by navArgs()

    private lateinit var feedDetailViewModel: FeedDetailViewModel
    private lateinit var feedDetailAdapter: FeedDetailAdapter

    private var socialFeedJob = SupervisorJob()
    private val socialFeedScope = CoroutineScope(Dispatchers.Main + socialFeedJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.dialogFullScreenSocialFeed
        )    // set a custom style to make the dialog fragment bigger
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_social_feed_detail, container, false)

        val challengeRepository = RepositoryController.getChallengeRepository()
        val userRepository = RepositoryController.getUserRepository()
        feedDetailViewModel = getViewModel { FeedDetailViewModel(challengeRepository, userRepository) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val (id, title, description) = arguments

        feed_detail_title.text = title

        if (description != "") {
            feed_detail_description.text = description
        } else {
            feed_detail_description.text = "Keine Beschreibung"
        }

        val userId = getUserId() ?: return

        setupParticipantList()
        showParticipants(id)

        socialFeedScope.launch {
            val challenge = feedDetailViewModel.getChallenge(id) ?: return@launch

            if (challenge.creatorId == userId) {
                // this challenge belongs to the current user

                //feed_accept_button.isEnabled = false
                feed_accept_button.visibility = View.GONE
                feed_detail_already_accepted_message.visibility = View.VISIBLE
                return@launch
            }

            val allChallenges = feedDetailViewModel.getUsersChallenges(userId) ?: return@launch
            for (c in allChallenges) {
                if (c.challengeId == challenge.challengeId) {
                    // the challenge is already in the active challenges of the current user
                    // so hide the accept button

                    //feed_accept_button.isEnabled = false
                    feed_accept_button.visibility = View.GONE
                    feed_detail_already_accepted_message.visibility = View.VISIBLE
                    return@launch
                }
            }

            val currentUser = feedDetailViewModel.getCurrentUser(userId) ?: return@launch

            feed_accept_button.setOnClickListener {
                acceptChallenge(challenge, currentUser)
            }

        }
    }

    private fun setupParticipantList() {
        feedDetailAdapter = FeedDetailAdapter()
        val linearLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        recycler_feed_detail_view.apply {
            setHasFixedSize(true)
            adapter = feedDetailAdapter
            layoutManager = linearLayoutManager
        }

        if (feedDetailAdapter.participants.isEmpty()) {
            // hide title if no participants
            feed_detail_recycler_title.visibility = View.GONE
            feed_detail_no_participants_message.visibility = View.VISIBLE
        }
    }

    //TODO: diese Methode könnten eigentlich auch in Utils so oft wie es gebraucht wird
    private fun getUserId(): String? {
        val sharedPrefs =
            requireActivity().getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""

        if (userId == "") {
            Toast.makeText(requireActivity(), R.string.wrong_user_id_error, Toast.LENGTH_SHORT).show()
            return null
        }

        return userId
    }

    private fun showParticipants(challengeId: String) {
        socialFeedScope.launch {
            val participants = measureTimeMillis({ time -> Timber.d("Get Participants took $time ms") }) {
                feedDetailViewModel.getParticipantsForChallenge(challengeId) ?: return@launch
            }

            feedDetailAdapter.participants = participants

            if (participants.isNotEmpty()) {
                // show title if not empty
                feed_detail_recycler_title.visibility = View.VISIBLE
                feed_detail_no_participants_message.visibility = View.GONE
            }

            // finished loading stuff; hide the loading circle and show the content
            loading_circle.visibility = View.GONE
            social_feed_detail_dialog.visibility = View.VISIBLE
        }
    }

    private fun acceptChallenge(challenge: UserChallenge, user: User) {
        // diasble button to prevent more clicks
        feed_accept_button.isEnabled = false

        feedDetailViewModel.acceptPublicChallenge(challenge, user)

        val snackbar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),   // uses the android content to attach to
            "Du hast diese Challenge erfolgreich angenommen!",
            Snackbar.LENGTH_SHORT
        )
        snackbar.view.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
        snackbar.show()

        // dismiss fragment
        requireActivity().findNavController(R.id.nav_host_fragment).popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        socialFeedJob.cancel()  // cancel all coroutines that might still be running to prevent memory leaks and crashes
    }
}