package com.example.challengecovid.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.challengecovid.R
import com.example.challengecovid.Utils
import com.example.challengecovid.adapter.RecyclerAdapter
import com.example.challengecovid.database.ChallengeDao
import com.example.challengecovid.database.ChallengeDatabase
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.repository.ChallengeRepository
import com.example.challengecovid.viewmodels.OverviewViewModel
import com.example.challengecovid.viewmodels.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.random.Random

class OverviewFragment : Fragment() {

    private lateinit var dataSource: ChallengeDao
    private lateinit var overviewViewModel: OverviewViewModel

    private lateinit var recyclerAdapter: RecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_overview, container, false)

        val application: Application = requireNotNull(this.activity).application
        val db = ChallengeDatabase.getInstance(application /*, coroutineScope*/) //TODO: should the db be instantiated in the application class for prepopulating?
        val repository = ChallengeRepository(db)

        // init viewmodel with datasource
        overviewViewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository)
        ).get(OverviewViewModel::class.java)

        //dataSource = db.challengeDao()
        //testDatabase()

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

        // see https://codelabs.developers.google.com/codelabs/kotlin-android-training-coroutines-and-room/#7
        add_button.setOnClickListener {
            //val randomNum = Random.nextInt()
            //Timber.tag("randomNumber").d(randomNum.toString())

            val dummyChallenge = Challenge(
                "New dummy challenge",
                "A new challenge was created! Good job!",
                R.drawable.ic_trophy,
                4,
                "easy",
                2f,
                Date().time
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

    private fun testDatabase() {
        Observable.fromCallable {
            //create test challenges
            val challenge1 = Challenge(
                "Custom Challenge1",
                "Custom Description1",
                R.drawable.test,
                3,
                "medium",
                5f,
                1234567 // in milliseconds
            )
            val challenge2 = Challenge(
                "Custom Challenge2",
                "Custom Description2",
                R.drawable.ic_star,
                5,
                "high",
                10f,
                5678930
            )

            // add the challenges to the database
            CoroutineScope(Dispatchers.Main).launch {
                with(dataSource){
                    this.insert(challenge1)
                    this.insert(challenge2)
                }
            }

            // fetch them from the db
            dataSource.getAllChallenges()

        }.doOnNext { list ->
            var finalString = ""
            list.value?.map { finalString += it.title + " - " }
            Timber.d(finalString)

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }


    companion object {
        private const val DEFAULT_NUMBER_COLUMNS = 2
    }
}