package com.example.challengecovid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
                feedAdapter.submitList(it)
            }
        })
    }

    private fun showChallengeDetails(challenge: UserChallenge) {
        val action = SocialFeedFragmentDirections.actionFeedToDetail(
            challengeId = challenge.challengeId,
            challengeTitle = challenge.title,
            challengeDescription = challenge.description
        )

        // navigate to another fragment on click
        requireActivity().findNavController(R.id.nav_host_fragment).navigate(action)
    }
}