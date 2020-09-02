package com.example.challengecovid.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.Constants
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.adapter.ChallengeClickListener
import com.example.challengecovid.adapter.OverviewAdapter
import com.example.challengecovid.model.BaseChallenge
import com.example.challengecovid.model.ChallengeType
import com.example.challengecovid.viewmodels.OverviewViewModel
import com.example.challengecovid.viewmodels.getViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.challenge_item.*
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.overview_content.*
import timber.log.Timber
import java.util.*


//TODO: noch alle Toasts aus den repositories und sonst wo, die nicht nötig sind, entfernen!!!
class OverviewFragment : Fragment() {

    private lateinit var overviewViewModel: OverviewViewModel
    private lateinit var overviewAdapter: OverviewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_overview, container, false)

        val challengeRepository = RepositoryController.getChallengeRepository()
        val userRepository = RepositoryController.getUserRepository()
        val application = requireNotNull(this.activity).application
        overviewViewModel = getViewModel { OverviewViewModel(challengeRepository, userRepository, application) }

        checkFirstTimeThisDay()

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

    private fun checkFirstTimeThisDay() {
        //TODO: da die Studie im gleichen Monat durchgeführt wird, ist das ausreichend!
        // Sonst müsste man außerdem noch Monat und Jahr vergleichen!
        val currentDay = Calendar.DAY_OF_MONTH

        val sharedPrefs =
            requireActivity().getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        // day of month starts with 1 so 0 is a good default to make sure it works the first time as well
        val lastDay = sharedPrefs?.getInt(Constants.PREFS_LAST_DAY, 0) ?: 0

        // get the id of the last daily challenge the day before (or null if the first time)
        val lastDailyChallengeId = sharedPrefs?.getString(Constants.PREFS_LAST_DAILY_CHALLENGE, null)

        if (currentDay > lastDay) {
            sharedPrefs.edit().putInt(Constants.PREFS_LAST_DAY, currentDay).apply()
            overviewViewModel.getRandomDailyChallenge(lastDailyChallengeId)

            // remove the old daily challenge from the users active challenges
            overviewViewModel.removeChallenge(lastDailyChallengeId ?: return)
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


    //TODO: material showcase view to show how to delete items??
    private fun setupObservers() {
        overviewViewModel.allChallenges.observe(viewLifecycleOwner, {
            val challengeList: MutableList<BaseChallenge> = (it ?: return@observe) as MutableList<BaseChallenge>

            val dailyChallenge = challengeList.find { challenge -> challenge.type == ChallengeType.DAILY_CHALLENGE }

            dailyChallenge?.let {
                // remove the daily challenge from the challenge list so it won't be shown twice
                challengeList.remove(dailyChallenge)

                name_challenge.text = dailyChallenge.title
                xp_challenge.text = String.format("%s XP", dailyChallenge.difficulty.points)
                description_challenge.text = dailyChallenge.description
            }

            icon_daily_challenge.setImageResource(R.drawable.icons8_parchment_80)

            daily_challenge.setOnClickListener {
                //TODO ?
                Toast.makeText(
                    requireActivity(),
                    "Du hast auf die Daily Challenge geklickt! Herzlichen Glückwunsch!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // update the list in the adapter with the new challenge list
            overviewAdapter.activeChallenges = challengeList
        })

        overviewViewModel.showDailyChallengeEvent.observe(viewLifecycleOwner, {
            if (it == true) {
                val snackbar = Snackbar.make(
                    //requireActivity().findViewById(android.R.id.content),   // uses the android content to attach to
                    overview_layout,
                    "Du hast eine neue Challenge erhalten!",
                    Snackbar.LENGTH_LONG
                )
                snackbar.view.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
                snackbar.show()

                // Reset state to make sure the toast is only shown once, even if the device has a configuration change.
                overviewViewModel.doneShowingSnackbar()
            }
        })
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

                val message = when (challenge.type) {
                    ChallengeType.SYSTEM_CHALLENGE -> "Wenn du diese Challenge löschst, kann sie für diese Woche nicht mehr erneut angenommen werden!"  //TODO: das stimmt im moment aber nicht lol!!
                    ChallengeType.USER_CHALLENGE -> "ACHTUNG:\nWenn diese Challenge öffentlich ist, wird nur deine eigene Version gelöscht! Um die Challenge auch aus den veröffentlichten Challenges zu löschen, musst du sie vor dem Löschen erst auf privat setzen!"
                    ChallengeType.DAILY_CHALLENGE -> "Eine Daily Challenge kann nicht gelöscht werden!"
                }

                with(AlertDialog.Builder(viewHolder.itemView.context)) {
                    setTitle("Challenge löschen?")
                    setMessage(message)
                    setPositiveButton("Löschen") { _, _ ->
                        // remove this item
                        overviewViewModel.removeChallenge(challenge.challengeId)
                        Toast.makeText(requireContext(), "Challenge gelöscht", Toast.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Abbrechen") { _, _ ->
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