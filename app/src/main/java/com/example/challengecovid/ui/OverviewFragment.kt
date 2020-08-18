package com.example.challengecovid.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.challengecovid.R
import com.example.challengecovid.Utils
import com.example.challengecovid.adapter.CategoryClickListener
import com.example.challengecovid.adapter.ChallengeClickListener
import com.example.challengecovid.adapter.RecyclerAdapter
import com.example.challengecovid.database.dao.ChallengeDao
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.database.repository.CategoryRepository
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.Difficulty
import com.example.challengecovid.database.repository.ChallengeRepository
import com.example.challengecovid.viewmodels.OverviewViewModel
import com.example.challengecovid.viewmodels.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class OverviewFragment : Fragment() {

    private lateinit var dataSource: ChallengeDao
    private lateinit var overviewViewModel: OverviewViewModel

    private lateinit var recyclerAdapter: RecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_overview, container, false)

        val application: Application = requireNotNull(this.activity).application
        val db = ChallengeAppDatabase.getInstance(application, CoroutineScope(Dispatchers.IO)) //TODO: should the db be instantiated in the application class for prepopulating?
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
            override fun onChallengeClick(challenge: Challenge) {
                Toast.makeText(context, "Clicked on ${challenge.title} (${challenge.challengeId})", Toast.LENGTH_LONG).show()

                // navigate to another fragment on click
                requireActivity().findNavController(R.id.nav_host_fragment).navigate(
                    R.id.action_global_navigation_map,
                    null,
                    NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setEnterAnim(R.anim.slide_to_left)
                        .setExitAnim(R.anim.fragment_close_exit)
                        .build()
                )
            }
        })

        // calculate the number of columns to use for the grid layout
        val numberOfColumns = this.context?.let { Utils.calculateNumberOfColumns(it) } ?: DEFAULT_NUMBER_COLUMNS

        // setup the Grid Layout with the recycler adapter
        list.apply {
            setHasFixedSize(true) //can improve performance if changes in content do not change the layout size of the RecyclerView
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(activity, numberOfColumns)
        }

        // see https://codelabs.developers.google.com/codelabs/kotlin-android-training-coroutines-and-room/#7
        add_button.setOnClickListener {
            //val randomNum = Random.nextInt()
            //Timber.tag("randomNumber").d(randomNum.toString())

            // test out a simple animation
            YoYo.with(Techniques.Wave)
                .duration(400)  // 400 ms
                .repeat(2)
                .playOn(it)

            val dummyChallenge = Challenge(
                "New dummy challenge",
                "A new challenge was created! Good job!",
                Difficulty.LEICHT,
                false,
                "Category ID this challenge belongs to",
                null,
                R.drawable.ic_trophy
            )

            overviewViewModel.addNewChallenge(dummyChallenge)
            /*
            val job = CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    dataSource.insert(dummyChallenge)
                }
            }
            */
        }
    }

    /**
     * Called when the fragment's activity has been created and this fragment's view hierarchy instantiated.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
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