package com.example.challengecovid.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.transition.TransitionInflater
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.challengecovid.App
import com.example.challengecovid.R
import com.example.challengecovid.Utils
import com.example.challengecovid.adapter.ChallengeClickListener
import com.example.challengecovid.adapter.RecyclerAdapter
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.database.dao.CategoryDao
import com.example.challengecovid.database.repository.CategoryRepository
import com.example.challengecovid.database.repository.ChallengeRepository
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.Difficulty
import com.example.challengecovid.viewmodels.OverviewViewModel
import com.example.challengecovid.viewmodels.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

//TODO: the contents in here should actually be in the challengeFragment
class OverviewFragment : Fragment() {

    private lateinit var categoryDataSource: CategoryDao
    private lateinit var overviewViewModel: OverviewViewModel

    private lateinit var recyclerAdapter: RecyclerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_overview, container, false)

        val application: Application = requireNotNull(this.activity).application
        val db = ChallengeAppDatabase.getInstance(
            application,
            CoroutineScope(Dispatchers.IO)
        )
        val challengeRepository = ChallengeRepository(db)
        val categoryRepository = CategoryRepository(db)

        // init viewmodel with datasource
        overviewViewModel = ViewModelProvider(
            this,
            ViewModelFactory(challengeRepository)
        ).get(OverviewViewModel::class.java)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerAdapter = RecyclerAdapter(object : ChallengeClickListener {
            override fun onChallengeClick(itemView: View, challenge: Challenge) {
                showCategoryDetails(itemView, challenge)
            }
        })

        // calculate the number of columns to use for the grid layout
        val numberOfColumns = this.context?.let { Utils.calculateNumberOfColumns(it) } ?: DEFAULT_NUMBER_COLUMNS

        // setup the Grid Layout with the recycler adapter
        //FIXME: this leaks memory for some reason
        recycler_category_list.apply {
            setHasFixedSize(true) //can improve performance if changes in content do not change the layout size of the RecyclerView
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(activity, numberOfColumns)

            // postpone the transitions to await loading of all list items before the shared element transitions returns
            // (otherwise the transition would only work on Exit but not on Return to this view!)
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // remove the listener instantly to prevent leaks!
                    recycler_category_list.viewTreeObserver.removeOnPreDrawListener(this)
                    // signals that the UI has been laid out and is ready for the transition
                    startPostponedEnterTransition()
                    return true
                }
            })
        }

        //TODO: move this to the challengeListView later
        add_button.setOnClickListener {
            addNewChallenge(it)
        }
    }

    /**
     * Called when the fragment's activity has been created and this fragment's view hierarchy instantiated.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    // see https://codelabs.developers.google.com/codelabs/kotlin-android-training-coroutines-and-room/#7
    private fun addNewChallenge(view: View) {
        //val randomNum = Random.nextInt()
        //Timber.tag("randomNumber").d(randomNum.toString())

        // test out a simple animation
        YoYo.with(Techniques.Wave)
            .duration(400)  // 400 ms
            .repeat(2)
            .playOn(view)

        val dummyChallenge = Challenge(
            "New dummy challenge",
            "A new challenge was created! Good job!",
            Difficulty.LEICHT,
            false,
            "Category ID this challenge belongs to",
            null,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.cherry_please_stay_isolated_1)
        )

        overviewViewModel.addNewChallenge(dummyChallenge)
    }

    private fun showCategoryDetails(itemView: View, challenge: Challenge) {
        val extras = FragmentNavigatorExtras(itemView to itemView.transitionName)
        val action = OverviewFragmentDirections.actionOverviewToDetail(
            title = challenge.title,
            description = challenge.description,
            imageName = challenge.challengeIcon
        )

        // set an exit transition
        exitTransition = TransitionInflater.from(context).inflateTransition(R.transition.grid_exit_transition)

        // navigate to another fragment on click
        requireActivity().findNavController(R.id.nav_host_fragment).navigate(action, extras)
        /*
        requireActivity().findNavController(R.id.nav_host_fragment).navigate(
            R.id.action_overview_to_detail,
            null,
            NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.fragment_close_exit)
                .build()
        )*/
    }

    private fun setupObservers() {
        overviewViewModel.challenges.observe(viewLifecycleOwner, Observer {
            it?.let {
                recyclerAdapter.submitList(it)  // update recyclerItems that have changed
            }
        })

        overviewViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "DB content changed!",
                    Snackbar.LENGTH_SHORT
                ).show()

                // Reset state to make sure the toast is only shown once, even if the device has a configuration change.
                overviewViewModel.doneShowingSnackbar()
            }
        })
    }


    companion object {
        private const val DEFAULT_NUMBER_COLUMNS = 2
    }
}