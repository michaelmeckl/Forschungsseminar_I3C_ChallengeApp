package com.example.challengecovid.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.adapter.ChallengeListAdapter
import com.example.challengecovid.viewmodels.ChallengeViewModel2
import com.example.challengecovid.viewmodels.ChallengesViewModel
import kotlinx.android.synthetic.main.fragment_challenges.*


class ChallengesFragment : Fragment() {

    private var root: View? = null
    private val challengesViewModel: ChallengesViewModel by viewModels()
    private lateinit var challengeViewModel2: ChallengeViewModel2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_challenges, container, false)

//        challengesViewModel.text.observe(viewLifecycleOwner, Observer {
//            text_challenges.text = it
//        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = recyclerview_challenges
        val adapter = ChallengeListAdapter(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Get a new or existing ViewModel from the ViewModelProvider.
        challengeViewModel2 = ViewModelProvider(this).get(ChallengeViewModel2::class.java)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        challengeViewModel2.allChallenges.observe(viewLifecycleOwner, Observer { challenges ->
            // Update the cached copy of the words in the adapter.
            challenges?.let { adapter.setChallenges(it) }
        })


//        button_first.setOnClickListener {
//            println("Hello Button")
//        }

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
                    challengeViewModel2.delete(adapter.getChallengeAt(viewHolder.adapterPosition))
                    Toast.makeText(requireContext(), "Challenge gelöscht", Toast.LENGTH_SHORT).show()
                }
                alertDialogBuilder.setNegativeButton("Nein") { _, _ ->
                    // User cancelled the dialog, so we will refresh the adapter to prevent hiding the item from UI
                    adapter.notifyItemChanged(viewHolder.adapterPosition)
                    Toast.makeText(requireContext(), "Challenge nicht gelöscht", Toast.LENGTH_SHORT).show()
                }
                alertDialogBuilder.show()
//                challengeViewModel2.delete(adapter.getChallengeAt(viewHolder.adapterPosition))
            }
        }).attachToRecyclerView(recyclerView)
    }

}