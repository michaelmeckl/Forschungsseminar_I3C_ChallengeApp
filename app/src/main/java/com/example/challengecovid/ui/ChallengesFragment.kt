package com.example.challengecovid.ui

import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.adapter.ChallengeListAdapter
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.database.repository.ChallengeRepository
import com.example.challengecovid.viewmodels.ChallengeListViewModel
import com.example.challengecovid.viewmodels.OverviewViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.fragment_challenges.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class ChallengesFragment : Fragment() {

    private lateinit var challengeListViewModel: ChallengeListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_challenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application: Application = requireNotNull(this.activity).application
        val db = ChallengeAppDatabase.getInstance(
            application,
            CoroutineScope(Dispatchers.IO)
        )
        val challengeRepository = ChallengeRepository(db)

        val listAdapter = ChallengeListAdapter(requireContext())
        recyclerview_challenges.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        challengeListViewModel = getViewModel { ChallengeListViewModel(challengeRepository) }

        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        challengeListViewModel.allChallenges.observe(viewLifecycleOwner, { it ->
            // Update the cached copy of the words in the adapter.
            it?.let {
                listAdapter.challenges = it
            }
        })

        fab_create_challenge.setOnClickListener {
            val newFragment: Fragment = CreateChallengeFragment()
            val transaction = requireFragmentManager().beginTransaction()

            transaction.replace(
                R.id.nav_host_fragment,
                newFragment
            )

            transaction.addToBackStack(null) // if written, this transaction will be added to backstack

            transaction.commit()

        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val alertDialogBuilder = AlertDialog.Builder(viewHolder.itemView.context)
                alertDialogBuilder.setTitle("Challenge löschen?")
                alertDialogBuilder.setPositiveButton("Ja") { _, _ ->
                    // remove this item
                    challengeListViewModel.delete(listAdapter.getChallengeAt(viewHolder.adapterPosition))
                    Toast.makeText(requireContext(), "Challenge gelöscht", Toast.LENGTH_SHORT).show()
                }
                alertDialogBuilder.setNegativeButton("Nein") { _, _ ->
                    // User cancelled the dialog, so we will refresh the adapter to prevent hiding the item from UI
                    listAdapter.notifyItemChanged(viewHolder.adapterPosition)
                    Toast.makeText(requireContext(), "Challenge nicht gelöscht", Toast.LENGTH_SHORT).show()
                }
                alertDialogBuilder.show()
//                challengeViewModel2.delete(adapter.getChallengeAt(viewHolder.adapterPosition))
            }
        }).attachToRecyclerView(recyclerview_challenges)
    }

}