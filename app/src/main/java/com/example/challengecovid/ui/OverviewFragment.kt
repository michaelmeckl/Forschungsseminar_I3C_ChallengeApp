package com.example.challengecovid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.example.challengecovid.adapter.CheckmarkClickListener
import com.example.challengecovid.adapter.OverviewAdapter
import com.example.challengecovid.model.BaseChallenge
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.ChallengeType
import com.example.challengecovid.model.User
import com.example.challengecovid.viewmodels.OverviewViewModel
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.getViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.challenge_item.*
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.overview_content.*
import timber.log.Timber
import java.util.*


class OverviewFragment : Fragment() {

    private lateinit var overviewViewModel: OverviewViewModel
    private lateinit var overviewAdapter: OverviewAdapter
    private lateinit var profileViewModel: ProfileViewModel

    private lateinit var _currentUser: User
    private lateinit var _challengeList: List<BaseChallenge>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_overview, container, false)

        val challengeRepository = RepositoryController.getChallengeRepository()
        val userRepository = RepositoryController.getUserRepository()
        val application = requireNotNull(this.activity).application
        overviewViewModel = getViewModel { OverviewViewModel(challengeRepository, userRepository, application) }
        profileViewModel = getViewModel { ProfileViewModel(userRepository, application) }


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
            }
        }, object : CheckmarkClickListener {
            override fun onCheckmarkClick(challenge: BaseChallenge) {
                setChallengeCompleted(challenge)
            }
        })


        recyclerview_overview.apply {
            setHasFixedSize(true)
            adapter = overviewAdapter
            layoutManager = linearLayoutManager
        }

        recyclerview_overview.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (overviewAdapter.activeChallenges.isEmpty()) {
                empty_recyclerview_overview.visibility = View.VISIBLE
            } else {
                empty_recyclerview_overview.visibility = View.GONE
                // TODO OMG Find another place to call this
                checkIfNewDayToSetCompletedToFalse()
            }

            /*
            // does not scroll to top :(
            if (oldTop < top) {
                // scroll to the top when the list gets bigger!
                recyclerview_overview.smoothScrollToPosition(0)
            }*/
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
            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .build()

            requireActivity().findNavController(R.id.nav_host_fragment)
                .navigate(OverviewFragmentDirections.actionOverviewToCreate())
        }
    }

    private fun checkIfNewDayToSetCompletedToFalse() {
        val sharedPrefs =
            requireActivity().getSharedPreferences(
                Constants.SHARED_PREFS_NAME,
                AppCompatActivity.MODE_PRIVATE
            )
        val currentDay = Calendar.DAY_OF_MONTH
        val lastDay = sharedPrefs.getInt(Constants.PREFS_LAST_DAY_CHALLENGES_RESET, 0)
        if (currentDay > lastDay && ::_challengeList.isInitialized) {
            sharedPrefs.edit().putInt(Constants.PREFS_LAST_DAY_CHALLENGES_RESET, currentDay).apply()
            overviewViewModel.setAllChallengesToNotCompleted(_challengeList)
        }
    }

    private fun checkChallengeCompletedFirstTimeThisDay() {

        val sharedPrefs =
            requireActivity().getSharedPreferences(
                Constants.SHARED_PREFS_NAME,
                AppCompatActivity.MODE_PRIVATE
            )

        val currentDay = Calendar.DAY_OF_MONTH
        val lastDay = sharedPrefs.getInt(Constants.PREFS_LAST_DAY_CHALLENGE_COMPLETED, 0)
        var counterOfConsecutiveDays = sharedPrefs.getInt(Constants.PREFS_COUNT_CONSECUTIVE_DAYS, 0)

        if (lastDay == currentDay - 1) {
            // CONSECUTIVE DAYS
            counterOfConsecutiveDays += 1
            sharedPrefs?.edit()?.putInt(Constants.PREFS_LAST_DAY_CHALLENGE_COMPLETED, currentDay)?.apply()
            sharedPrefs?.edit()?.putInt(Constants.PREFS_COUNT_CONSECUTIVE_DAYS, counterOfConsecutiveDays)?.apply()
        } else {
            sharedPrefs?.edit()?.putInt(Constants.PREFS_LAST_DAY_CHALLENGE_COMPLETED, currentDay)?.apply()
            sharedPrefs?.edit()?.putInt(Constants.PREFS_COUNT_CONSECUTIVE_DAYS, 1)?.apply()
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

        val sharedPrefs = requireActivity().getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)

        if (sharedPrefs.getBoolean(Constants.PREFS_FIRST_TIME_CHALLENGE_COMPLETED, true)) {
            // this is the first time this user has ticked the 'completed' button
            AlertDialog.Builder(requireContext())
                .setTitle("Challenge abschließen")
                .setMessage("Hier kannst du diese Challenge als \"erledigt\" markieren und dafür Erfahrungspunkte bekommen. Beachte aber, dass du diese Entscheidung nicht mehr rückgängig machen kannst für den Rest des Tages! Möchtest du diese Challenge als abgeschlossen markieren?")
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    sharedPrefs.edit()
                        .putBoolean(Constants.PREFS_FIRST_TIME_CHALLENGE_COMPLETED, false).apply()
                    showChallengeCompletedDialog(challenge)
                }
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .show()
        } else {
            val completedChallengesCount = sharedPrefs.getInt(Constants.PREFS_COUNT_COMPLETED_CHALLENGES, 0)
            sharedPrefs.edit().putInt(Constants.PREFS_COUNT_COMPLETED_CHALLENGES, completedChallengesCount + 1).apply()

            checkChallengeCompletedFirstTimeThisDay()

            overviewViewModel.setChallengeCompleted(challenge)
            //Timber.d(profileViewModel.currentUser.value.toString())

            updatePointsAndLevel(_currentUser, challenge)
        }
    }


    private fun showChallengeCompletedDialog(challenge: BaseChallenge) {
        AlertDialog.Builder(requireContext())
            .setTitle("Challenge abgeschlossen!")
            .setMessage("Du kannst sie jetzt aus der Übersicht löschen, indem du sie zur Seite wischst. Alternativ kannst du die Challenge auch behalten und sie morgen dann erneut abschließen.")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val sharedPrefs = requireActivity().getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
                val completedChallengesCount = sharedPrefs.getInt(Constants.PREFS_COUNT_COMPLETED_CHALLENGES, 0)
                sharedPrefs.edit().putInt(Constants.PREFS_COUNT_COMPLETED_CHALLENGES, completedChallengesCount + 1).apply()
                checkChallengeCompletedFirstTimeThisDay()
                overviewViewModel.setChallengeCompleted(challenge)
                updatePointsAndLevel(_currentUser, challenge)
            }
            .show()
    }


    private fun setupObservers() {
        overviewViewModel.allChallenges.observe(viewLifecycleOwner, {
            val challengeList: MutableList<BaseChallenge> =
                (it ?: return@observe) as MutableList<BaseChallenge>
            _challengeList = challengeList

            val dailyChallenge = challengeList.find { challenge -> challenge.type == ChallengeType.DAILY_CHALLENGE }

            dailyChallenge?.let {
                // remove the daily challenge from the challenge list so it won't be shown twice
                challengeList.remove(dailyChallenge)
                name_challenge.text = dailyChallenge.title
                xp_challenge.text = String.format("%s XP", dailyChallenge.difficulty.points)

                if (!dailyChallenge.completed) {
                    description_challenge.text = dailyChallenge.description
                    icon_challenge.visibility = View.VISIBLE
                    icon_challenge_completed.visibility = View.GONE
                } else {
                    description_challenge.visibility = View.GONE
                    description_challenge_completed.visibility = View.VISIBLE
                    icon_challenge.visibility = View.INVISIBLE
                    icon_challenge_completed.visibility = View.VISIBLE
                }
            }

            icon_daily_challenge.setImageResource(R.drawable.icons8_parchment_80)

            daily_challenge.setOnClickListener {
                if (dailyChallenge != null) {
                    if (!dailyChallenge.completed) {
                        val toast = Toast.makeText(
                            requireActivity(),
                            "Das ist deine Tagesaufgabe! Sie ist nur heute verfügbar, versuch also sie möglichst schnell abzuschließen!",
                            Toast.LENGTH_LONG
                        )
                        toast.view.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
                        toast.view.setPadding(8, 4, 8, 4)
                        toast.show()

                    } else {
                        val toast = Toast.makeText(
                            requireActivity(),
                            "Glückwunsch, du hast deine Tagesaufgabe abgeschlossen! Morgen bekommst du wieder eine neue.",
                            Toast.LENGTH_LONG
                        )
                        toast.view.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
                        toast.view.setPadding(8, 4, 8, 4)
                        toast.show()

                    }
                }
            }
            icon_challenge.setOnClickListener {
                if (dailyChallenge != null) {
                    setChallengeCompleted(dailyChallenge)
                }
            }

            // update the list in the adapter with the new challenge list
            overviewAdapter.activeChallenges = challengeList
        })


        profileViewModel.currentUser.observe(viewLifecycleOwner, {it ->
            it?.let {
                _currentUser = it
            }
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

                // Reset state to make sure the snackbar is only shown once, even if the device has a configuration change.
                overviewViewModel.doneShowingSnackbar()
            }
        })
    }

    private fun showChallengeDetails(challenge: BaseChallenge) {
        val action = OverviewFragmentDirections.actionOverviewToDetail(
            id = challenge.challengeId,
            title = challenge.title,
            description = challenge.description,
            type = challenge.type,
            difficulty = challenge.difficulty.toString(),
            completed = challenge.completed
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

//          FIXME: This can sometimes throw an indexoutofboundexception. Can't reproduce the error as of now.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val challenge = overviewAdapter.getChallengeAt(viewHolder.adapterPosition)

                val message = when (challenge.type) {
                    ChallengeType.SYSTEM_CHALLENGE -> "Wenn du diese Challenge löschst, kann sie für diese Woche nicht mehr erneut angenommen werden!"
                    ChallengeType.USER_CHALLENGE -> "ACHTUNG:\nWenn diese Challenge öffentlich ist, wird nur die eigene Version gelöscht! Wenn du der Ersteller der Challenge bist und sie auch aus den veröffentlichten Challenges löschen willst, musst du sie vor dem Löschen erst auf privat setzen!"
                    ChallengeType.DAILY_CHALLENGE -> "Eine Daily Challenge kann nicht gelöscht werden!"
                }

                with(AlertDialog.Builder(viewHolder.itemView.context)) {
                    setTitle("Challenge löschen?")
                    setMessage(message)
                    setPositiveButton("Löschen") { _, _ ->
                        // remove this item
                        if (challenge.type == ChallengeType.SYSTEM_CHALLENGE) {
                            //TODO: das ist ein "hack" um dem Nutzer zu simulieren, dass er die Challenge diese Woche nicht mehr
                            // annehmen kann (ohne sie dafür ganz löschen zu müssen)
                            overviewViewModel.hideChallenge(challenge as Challenge)
                        } else {
                            overviewViewModel.removeChallenge(challenge.challengeId)
                        }
                        val toast = Toast.makeText(requireContext(), "Challenge gelöscht", Toast.LENGTH_SHORT)
                        toast.view.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
                        toast.view.setPadding(8, 4, 8, 4)
                        toast.show()

                    }
                    setNegativeButton("Abbrechen") { _, _ ->
                        // User cancelled the dialog, so we will refresh the adapter to prevent hiding the item from UI
                        overviewAdapter.notifyItemChanged(viewHolder.adapterPosition)
                        //Toast.makeText(requireContext(), "Challenge nicht gelöscht", Toast.LENGTH_SHORT).show()
                    }
                    show()
                }
            }

        }).attachToRecyclerView(recyclerview_overview)
    }


    // LEVEL STUFF STARTING HERE

    //helper function
    private fun maxPointsReached(currentPoints: Int, maxPoints: Int): Boolean {
        return currentPoints >= maxPoints
    }

    //call when challenge got cleared
    private fun updatePointsAndLevel(user: User, challenge: BaseChallenge) {
        var currentPoints = user.points
        val currentLevel = user.level

        val sharedPrefs = requireActivity().getSharedPreferences(
            Constants.SHARED_PREFS_NAME,
            AppCompatActivity.MODE_PRIVATE
        )

        // save total amount of xp for achievements in profile fragment
        val prevTotalXPValue = sharedPrefs.getInt(Constants.PREFS_COUNT_TOTAL_XP, 0)
        sharedPrefs.edit().putInt(
            Constants.PREFS_COUNT_TOTAL_XP,
            prevTotalXPValue + challenge.difficulty.points
        ).apply()



        if (currentLevel >= levelsMap.size) {
            if (sharedPrefs.getBoolean(Constants.PREFS_FIRST_TIME_MAX_LEVEL_REACHED, true)) {
                // this is the first time this user has reached max level
                val toast = Toast.makeText(
                    requireActivity(),
                    "Du hast das maximale Level erreicht!",
                    Toast.LENGTH_LONG
                )
                toast.view.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
                toast.view.setPadding(8, 4, 8, 4)
                toast.show()
                sharedPrefs.edit().putBoolean(Constants.PREFS_FIRST_TIME_MAX_LEVEL_REACHED, false).apply()
            }
            return
        }

        val currentMaxPoints = levelsMap[currentLevel] ?: return
        currentPoints += challenge.difficulty.points


        if (maxPointsReached(currentPoints, currentMaxPoints)) {
            //Levelup (reset points)
            currentPoints -= currentMaxPoints

            // Inform user that he unlocked new avatars/chars
            when {
                currentLevel + 1 == 5 -> {
                    val toast = Toast.makeText(
                        requireActivity(),
                        "Glückwunsch! Du bist jetzt Level 5! Du hast 2 neue Avatare freigeschaltet!",
                        Toast.LENGTH_LONG
                    )
                    toast.view.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
                    toast.view.setPadding(8, 4, 8, 4)
                    toast.show()

                }
                currentLevel + 1 == 10 -> {
                    val toast = Toast.makeText(
                        requireActivity(),
                        "Glückwunsch! Du bist jetzt Level 10! Du hast die letzten 2 Avatare freigeschaltet!",
                        Toast.LENGTH_LONG
                    )
                    toast.view.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
                    toast.view.setPadding(8, 4, 8, 4)
                    toast.show()
                }
                else -> {
                    val toast = Toast.makeText(requireActivity(), "Glückwunsch! du bist jetzt Level ${currentLevel + 1}!", Toast.LENGTH_SHORT)
                    toast.view.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
                    toast.view.setPadding(8, 4, 8, 4)
                    toast.show()
                }
            }



            // send a firebase in-app-message to the user to congratulate him!
//            Firebase.analytics.logEvent("level_up", null)
//            Firebase.inAppMessaging.triggerEvent("level_up")
            // send a firebase in-app-message to the user to congratulate him! //TODO works only once a day unfortunately
            //Firebase.analytics.logEvent("level_up", null)
            //Firebase.inAppMessaging.triggerEvent("level_up")

            //TODO: unlock icon methode
            // TODO: show popup
            //  or show snackbar attached to android.R.id.content, like:
            /*
            val snackbar = Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),   // uses the android content to attach to
                    "Du bist ein Level aufgestiegen! Mach weiter so!",
                    Snackbar.LENGTH_LONG
                )
                snackbar.view.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
                snackbar.show()
             */

            profileViewModel.updateUserPoints(currentPoints)
            profileViewModel.updateUserLevel(currentLevel + 1)
        } else {
            profileViewModel.updateUserPoints(currentPoints)
        }

    }

    companion object {
        // map with necessary xp for levelup per level
        val levelsMap = hashMapOf(
            1 to 15,
            2 to 20,
            3 to 25,
            4 to 30,
            5 to 35,
            6 to 40,
            7 to 45,
            8 to 50,
            9 to 55,
            10 to 60,
            11 to 70,
            12 to 80,
            13 to 90,
            14 to 100,
            15 to 150,
            16 to 180,
            17 to 200,
            18 to 250,
            19 to 350,
            20 to 500
        )
    }

}
