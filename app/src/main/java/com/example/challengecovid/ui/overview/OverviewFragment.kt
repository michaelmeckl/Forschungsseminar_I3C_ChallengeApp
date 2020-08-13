package com.example.challengecovid.ui.overview

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.challengecovid.R
import com.example.challengecovid.Utils
import com.example.challengecovid.adapter.RecyclerAdapter
import com.example.challengecovid.database.ChallengeDao
import com.example.challengecovid.database.ChallengeDatabase
import com.example.challengecovid.database.Challenge
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_challenges.*
import kotlinx.android.synthetic.main.fragment_overview.*
import java.util.*

class OverviewFragment : Fragment() {

    private lateinit var db: ChallengeDatabase
    private lateinit var dataSource: ChallengeDao
    private lateinit var overviewViewModel: OverviewViewModel

    private lateinit var recyclerAdapter: RecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_overview, container, false)

        val application: Application = requireNotNull(this.activity).application
        db = ChallengeDatabase.getInstance(application)
        dataSource = db.challengeDao()

        // init viewmodel with context and datasource
        overviewViewModel = ViewModelProvider(
            this,
            OverviewViewmodelFactory(dataSource, application)
        ).get(OverviewViewModel::class.java)

        testDatabase()
        setupObservers()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerAdapter = RecyclerAdapter()
        // calculate the number of columns to use for the grid layout
        val numberOfColumns = this.context?.let { Utils.calculateNumberOfColumns(it) } ?: DEFAULT_NUMBER_COLUMNS

        // setup the Grid Layout with the recycler adapter
        list.apply {
            setHasFixedSize(true) //can improve performance if changes in content do not change the layout size of the RecyclerView
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(activity, numberOfColumns)
        }

        add_button.setOnClickListener {
            /*
            val dummyChallenge = Challenge(5656565, "New dummy challenge", "A new challenge was created! Good job!", R.drawable.ic_trophy, 4, "easy", 2f, Date().time)
            dataSource.insert(dummyChallenge)
            
             */
        }
    }

    private fun setupObservers() {
        overviewViewModel.challenges.observe(viewLifecycleOwner, Observer {
            it?.let {
                //TODO: does this work with ui mode change or crash?
                recyclerAdapter.submitList(it)  // update recyclerItems that have changed
            }
        })

        overviewViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),   //TODO: does this work?
                    "DB content changed!",
                    Snackbar.LENGTH_SHORT
                ).show()

                // Reset state to make sure the toast is only shown once, even if the device has a configuration change.
                overviewViewModel.doneShowingSnackbar()
            }
        })
    }

    private fun testDatabase() {
        Observable.fromCallable {
            //create test challenges
            val challenge1 = Challenge(
                5773,
                "Custom Challenge1",
                "Custom Description1",
                R.drawable.test,
                3,
                "medium",
                5f,
                1234567 // in milliseconds
            )
            val challenge2 = Challenge(
                24982,
                "Custom Challenge2",
                "Custom Description2",
                R.drawable.ic_star,
                5,
                "high",
                10f,
                5678930
            )

            // add the challenges to the database
            with(dataSource){
                this.insert(challenge1)
                this.insert(challenge2)
            }

            // fetch them from the db
            dataSource.getAllChallenges()

        }.doOnNext { list ->
            var finalString = ""
            list.value?.map { finalString += it.title +  " - " }
            Log.d("DB_Test", finalString)

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun createDummyRecyclerList(): List<Challenge> {
        val challengeList = mutableListOf<Challenge>()

        //val drawable: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.test, null)

        for (i in 1..10) {
            challengeList += Challenge(
                i,
                "Custom Challenge",
                "Custom Description",
                R.drawable.test,
                3,
                "medium",
                10f,
                238956
            )
        }

        return challengeList
    }

    companion object {
        private const val DEFAULT_NUMBER_COLUMNS = 2
    }
}