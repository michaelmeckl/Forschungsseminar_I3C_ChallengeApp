package com.example.challengecovid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challengecovid.Constants
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.adapter.FeedDetailAdapter
import com.example.challengecovid.model.User
import com.example.challengecovid.model.UserChallenge
import com.example.challengecovid.viewmodels.FeedDetailViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.fragment_social_feed_detail.*
import kotlinx.android.synthetic.main.social_feed_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SocialFeedDetail : DialogFragment() {

    private val arguments: SocialFeedDetailArgs by navArgs()

    private lateinit var feedDetailViewModel: FeedDetailViewModel
    private lateinit var feedDetailAdapter: FeedDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.dialogFullScreenSocialFeed)    // set a custom style to make the dialog fragment bigger
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_social_feed_detail, container, false)

        val challengeRepository = RepositoryController.getChallengeRepository()
        val userRepository = RepositoryController.getUserRepository()
        feedDetailViewModel = getViewModel { FeedDetailViewModel(challengeRepository, userRepository) }

        return root
    }

    //TODO: runBlocking with a loading progressbar to prevent ugly ui change??
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

        CoroutineScope(Dispatchers.Main).launch {
            val challenge = feedDetailViewModel.getChallenge(id) ?: return@launch

            if (challenge.creatorId == userId) {
                // this challenge belongs to the current user
                feed_accept_button.isEnabled = false
                return@launch
            }

            val allChallenges = feedDetailViewModel.getUsersChallenges(userId) ?: return@launch
            for (c in allChallenges) {
                if (c.challengeId == challenge.challengeId) {
                    //Toast.makeText(requireActivity(), "Du hast diese Challenge schon angenommen!", Toast.LENGTH_SHORT).show()

                    // hide accept button
                    feed_accept_button.isEnabled = false
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
        }
    }

    //TODO: diese Methode könnten eigentlich auch in Utils so oft wie es gebraucht wird
    private fun getUserId(): String? {
        val sharedPrefs = requireActivity().getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""

        if (userId == "") {
            Toast.makeText(requireActivity(), R.string.wrong_user_id_error, Toast.LENGTH_SHORT).show()
            return null
        }

        return userId
    }

    private fun showParticipants(challengeId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val participants = feedDetailViewModel.getParticipantsForChallenge(challengeId) ?: return@launch
            feedDetailAdapter.participants  = participants

            if (participants.isNotEmpty()) {
                // show title if not empty
                feed_detail_recycler_title.visibility = View.VISIBLE
            }
        }
    }


    /**
     * TODO:
     *  2. eine angenommene Challenge sollte nicht mehr veröffentlicht werden können!! -> vllt den button nur anzeigen wenn current user id die gleiche wie die creator id der challenge ist (in challenge detail in overview fragment)
     *  5. wenn sie in overview gelöscht wird, sollte der nutzer auch aus den participants verschwinden!!!
     *      -> challengeRepository.removeParticipant()
     *  7. angenommene challenge sollte nicht bearbeitbar sein von anderen nutzern
     */
    private fun acceptChallenge(challenge: UserChallenge, user: User) {
        Toast.makeText(requireContext(), "Challenge accepted!", Toast.LENGTH_SHORT).show()

        feedDetailViewModel.acceptPublicChallenge(challenge, user)

        // dismiss fragment
        requireActivity().findNavController(R.id.nav_host_fragment).popBackStack()
    }

}