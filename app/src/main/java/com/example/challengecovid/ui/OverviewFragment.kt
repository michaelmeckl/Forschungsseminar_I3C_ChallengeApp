package com.example.challengecovid.ui

import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.challengecovid.R
import com.example.challengecovid.adapter.UserChallengeAdapter
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.database.repository.ChallengeRepository
import com.example.challengecovid.viewmodels.OverviewViewModel
import com.example.challengecovid.viewmodels.getViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class OverviewFragment : Fragment() {

    private lateinit var overviewViewModel: OverviewViewModel
    private lateinit var challengeListAdapter: UserChallengeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_overview, container, false)

        val application: Application = requireNotNull(this.activity).application
        val db = ChallengeAppDatabase.getInstance(application, CoroutineScope(Dispatchers.IO))
        val challengeRepository = ChallengeRepository(db)

        overviewViewModel = getViewModel { OverviewViewModel(challengeRepository) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        challengeListAdapter = UserChallengeAdapter()
        recyclerview_challenges.apply {
            setHasFixedSize(true)
            adapter = challengeListAdapter
        }

        setupObservers()
        setupSwipeListener()

        fab_create_challenge.setOnClickListener {
            /*
            // add a simple animation
            YoYo.with(Techniques.Wobble)
                .duration(200)  // 300 ms
                .playOn(fab_create_challenge)
            */
            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .build()

            requireActivity().findNavController(R.id.nav_host_fragment)
                .navigate(OverviewFragmentDirections.actionOverviewToCreate())
        }
    }

    private fun setupObservers() {
        overviewViewModel.allChallenges.observe(viewLifecycleOwner, { it ->
            it?.let {
                // update the list in the adapter with the new challenge list
                challengeListAdapter.userChallenges = it
            }
        })

        overviewViewModel.showSnackBarEvent.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),   // uses the android content to attach to
                    "Challenge content in the DB has changed!",
                    Snackbar.LENGTH_SHORT
                ).show()

                // Reset state to make sure the toast is only shown once, even if the device has a configuration change.
                overviewViewModel.doneShowingSnackbar()
            }
        })
    }

    private fun setupSwipeListener() {

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                with(AlertDialog.Builder(viewHolder.itemView.context)) {
                    setTitle("Challenge löschen?")
                    setPositiveButton("Ja") { _, _ ->
                        // remove this item
                        overviewViewModel.removeChallenge(challengeListAdapter.getChallengeAt(viewHolder.adapterPosition))
                        Toast.makeText(requireContext(), "Challenge gelöscht", Toast.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Nein") { _, _ ->
                        // User cancelled the dialog, so we will refresh the adapter to prevent hiding the item from UI
                        challengeListAdapter.notifyItemChanged(viewHolder.adapterPosition)
                        Toast.makeText(requireContext(), "Challenge nicht gelöscht", Toast.LENGTH_SHORT).show()
                    }
                    show()
                }
            }

        }).attachToRecyclerView(recyclerview_challenges)
    }
}