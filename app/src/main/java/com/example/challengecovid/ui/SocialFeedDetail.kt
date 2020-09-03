package com.example.challengecovid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challengecovid.Constants
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.adapter.FeedDetailAdapter
import com.example.challengecovid.viewmodels.FeedDetailViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.fragment_social_feed_detail.*
import kotlinx.android.synthetic.main.social_feed_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val (id, title, description) = arguments

        feed_detail_title.text = title

        if (description != "") {
            feed_detail_description.text = description
        } else {
            feed_detail_description.text = "Keine Beschreibung"
        }

        feedDetailAdapter = FeedDetailAdapter()
        val linearLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        recycler_feed_detail_view.apply {
            setHasFixedSize(true)
            adapter = feedDetailAdapter
            layoutManager = linearLayoutManager
        }

        showParticipants(id)

        feed_accept_button.setOnClickListener {
            acceptChallenge(id)
        }
    }

    private fun showParticipants(challengeId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val participants = feedDetailViewModel.getParticipantsForChallenge(challengeId) ?: return@launch
            feedDetailAdapter.participants  = participants
        }
    }


    /**
     * TODO:
     *  1. eine challenge sollte nur angenommen werden können wenn man sie noch nicht bei den eigenen active Challenges schon hat -> vorher überprüfen in der Methode unten
     *  2. eine angenommene Challenge sollte nicht mehr veröffentlicht werden können!! -> vllt den button nur anzeigen wenn current user id die gleiche wie die creator id der challenge ist (in challenge detail in overview fragment)
     *  3. testen wies mit mehr teilnehmern aussieht
     *  4. (button mit accept nur für challenges von anderen nutzern anzeigen?) -> könnte durch 1. schon gefixt werden
     *  5. wenn sie in overview gelöscht wird, sollte der nutzer auch aus den participants verschwinden!!!
     *      -> challengeRepository.removeParticipant()
     */

    private fun acceptChallenge(challengeId: String) {
        //TODO: die nächsten 3 zeilen könnten eigentlich auch in Utils so oft wie es gebraucht wird
        val sharedPrefs = requireActivity().getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""

        if (userId == "") {
            Toast.makeText(requireActivity(), R.string.wrong_user_id_error, Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(requireContext(), "Challenge accepted!", Toast.LENGTH_SHORT).show()

        feedDetailViewModel.acceptPublicChallenge(challengeId, userId)
    }

}