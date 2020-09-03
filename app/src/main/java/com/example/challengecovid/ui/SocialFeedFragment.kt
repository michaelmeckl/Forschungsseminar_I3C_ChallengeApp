package com.example.challengecovid.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challengecovid.Constants
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.adapter.ChallengeFeedAdapter
import com.example.challengecovid.adapter.ChallengeFeedClickListener
import com.example.challengecovid.model.UserChallenge
import com.example.challengecovid.viewmodels.FeedViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.fragment_social_feed.*

class SocialFeedFragment : Fragment() {

    private lateinit var feedViewModel: FeedViewModel
    private lateinit var feedAdapter: ChallengeFeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_social_feed, container, false)

        val challengeRepository = RepositoryController.getChallengeRepository()
        val userRepository = RepositoryController.getUserRepository()
        feedViewModel = getViewModel { FeedViewModel(challengeRepository, userRepository) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedAdapter = ChallengeFeedAdapter(object : ChallengeFeedClickListener {
            override fun onChallengeClick(challenge: UserChallenge) {
                showChallengeDetails(challenge)
            }
        })

        recycler_social_feed.apply {
            setHasFixedSize(true)
            adapter = feedAdapter
            layoutManager = LinearLayoutManager(activity ?: return)
        }

        setupObservers()
    }

    private fun setupObservers() {
        feedViewModel.publicChallenges.observe(viewLifecycleOwner, { it ->
            it?.let {
                // update the list in the adapter with the new challenge list
                feedAdapter.publicChallenges = it
            }
        })
    }

    private fun showChallengeDetails(challenge: UserChallenge) {
        //TODO: make this acceptible? or show a detail with everybody that is already in that challenge and a button to accept?
        // or show the accept button directly in the list?
        Toast.makeText(requireActivity(), "You clicked on challenge ${challenge.title}", Toast.LENGTH_SHORT).show()

        with(AlertDialog.Builder(requireActivity())) {
            setTitle("Challenge annehmen?")
            setMessage("Möchtest du diese Challenge annehmen?")  //TODO
            setPositiveButton("Ja") { _, _ ->
                //TODO: die nächsten 3 zeilen könnten eigentlich auch in Utils so oft wie es gebraucht wird
                val sharedPrefs = requireActivity().getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
                val userId = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""

                if (userId == "") {
                    Toast.makeText(requireActivity(), R.string.wrong_user_id_error, Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                Toast.makeText(requireContext(), "Challenge accepted!", Toast.LENGTH_SHORT).show()
                feedViewModel.acceptPublicChallenge(challenge, userId)
            }
            setNegativeButton("Nein") { _, _ -> }
            show()
        }
    }
}