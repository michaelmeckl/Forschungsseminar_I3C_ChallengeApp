package com.example.challengecovid.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.adapter.ChallengeClickListener
import com.example.challengecovid.adapter.OverviewAdapter
import com.example.challengecovid.model.BaseChallenge
import com.example.challengecovid.viewmodels.OverviewViewModel
import com.example.challengecovid.viewmodels.getViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_overview.*

import timber.log.Timber

class OverviewFragment : Fragment() {

    private lateinit var overviewViewModel: OverviewViewModel
    private lateinit var overviewAdapter: OverviewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_overview, container, false)

        val challengeRepository = RepositoryController.getChallengeRepository()
        val userRepository = RepositoryController.getUserRepository()
        val application = requireNotNull(this.activity).application
        overviewViewModel = getViewModel { OverviewViewModel(challengeRepository, userRepository, application) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager =
            LinearLayoutManager(activity ?: return)   // return early if not attached to an activity
        //linearLayoutManager.stackFromEnd = true     // insert items at the bottom instead of top

        overviewAdapter = OverviewAdapter(object : ChallengeClickListener {
            override fun onChallengeClick(challenge: BaseChallenge) {
                showChallengeDetails(challenge)
                setChallengeCompleted(challenge)    //TODO: nur bei klick auf completed haken oder so, nicht auf ganzes!
            }
        })

        recyclerview_overview.apply {
            setHasFixedSize(true)
            adapter = overviewAdapter
            layoutManager = linearLayoutManager
        }

        //TODO: does not scroll to top :(
        recyclerview_overview.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (oldTop < top) {
                // scroll to the top when the list gets bigger!
                recyclerview_overview.smoothScrollToPosition(0)
            }
        }
        /*
        // Scroll to top on new challenges
        challengeListAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                recyclerview_overview.smoothScrollToPosition(0)
            }
        })
        */

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

    //TODO: für firestore ui und firestoreadapter
    /*
    private open fun newAdapter(): RecyclerView.Adapter<*>? {
        val options: FirestoreRecyclerOptions<Chat> = Builder<Chat>()
            .setQuery(sChatQuery, Chat::class.java)
            .setLifecycleOwner(this)
            .build()
        return object : FirestoreRecyclerAdapter<Chat?, ChatHolder?>(options) {
            @NonNull
            fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ChatHolder? {
                return ChatHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.message, parent, false)
                )
            }

            protected fun onBindViewHolder(@NonNull holder: ChatHolder, position: Int, @NonNull model: Chat?) {
                holder.bind(model)
            }

            fun onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
                mEmptyListMessage.setVisibility(if (getItemCount() === 0) View.VISIBLE else View.GONE)
            }
        }
    }

     */

    private fun setChallengeCompleted(challenge: BaseChallenge) {

        if (challenge.completed) {
            Timber.d("Skipping setChallengeCompleted because challenge.completed = true")
            return
        }
        Timber.d("setChallengeCompleted, ${challenge.completed} before")
        overviewViewModel.setChallengeCompleted(challenge)
        Timber.d("setChallengeCompleted, ${challenge.completed} after")

//        challengeListAdapter.notifyDataSetChanged()

    }


    private fun setupObservers() {
        overviewViewModel.allChallenges.observe(viewLifecycleOwner, { it ->
            it?.let {
                // update the list in the adapter with the new challenge list
                overviewAdapter.activeChallenges = it
            }
        })

        /*
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
        })*/
    }

    private fun showChallengeDetails(challenge: BaseChallenge) {
        val action = OverviewFragmentDirections.actionOverviewToDetail(
            id = challenge.challengeId,
            title = challenge.title,
            description = challenge.description,
            type = challenge.type
        )

        // navigate to another fragment on click
        requireActivity().findNavController(R.id.nav_host_fragment).navigate(action)
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
                val challenge = overviewAdapter.getChallengeAt(viewHolder.adapterPosition)

                with(AlertDialog.Builder(viewHolder.itemView.context)) {
                    setTitle("Challenge löschen?")
                    setMessage("ACHTUNG: Wenn diese Challenge öffentlich ist, wird nur deine eigene Version gelöscht! Um die Challenge auch aus den veröffentlichten Challenges zu löschen, musst du sie vor dem Löschen erst auf privat setzen!")
                    setPositiveButton("Ja") { _, _ ->
                        // remove this item
                        overviewViewModel.removeChallenge(challenge)
                        Toast.makeText(requireContext(), "Challenge gelöscht", Toast.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Nein") { _, _ ->
                        // User cancelled the dialog, so we will refresh the adapter to prevent hiding the item from UI
                        overviewAdapter.notifyItemChanged(viewHolder.adapterPosition)
                        Toast.makeText(requireContext(), "Challenge nicht gelöscht", Toast.LENGTH_SHORT).show()
                    }
                    show()
                }
            }

        }).attachToRecyclerView(recyclerview_overview)
    }
}