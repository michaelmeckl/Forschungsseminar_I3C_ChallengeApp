package com.example.challengecovid.ui.overview

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.challengecovid.R
import com.example.challengecovid.Utils
import com.example.challengecovid.adapter.RecyclerAdapter
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.CoronaStatistics
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.list_item_template.*
import kotlinx.android.synthetic.main.list_item_template.view.*

class OverviewFragment : Fragment() {

    private val overviewViewModel: OverviewViewModel by viewModels()
    private lateinit var recyclerAdapter: RecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_overview, container, false)

        setupObservers()

        //TODO: should be somewhere where internet request have to be made, not here!
        /*
        context?.let {
            if (Utils.isNetworkConnected(it)) {
                // do api stuff?
            } else {
                AlertDialog.Builder(it)
                    .setTitle("No Internet Connection")
                    .setMessage("Please check your internet connection and try again")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.ok) { _, _ -> }
                    .show()
            }
        }*/

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerList = createDummyRecyclerList() //TODO: this should come from the database later
        recyclerAdapter = RecyclerAdapter()
        recyclerAdapter.challengeList = recyclerList

        // calculate the number of columns used for the grid or the default number if this fragment has no context
        val numberOfColumns =
            this.context?.let { Utils.calculateNumberOfColumns(it) } ?: DEFAULT_NUMBER_COLUMNS

        // setup the Grid Layout with the recycler adapter
        list.apply {
            setHasFixedSize(true) //can improve performance if changes in content do not change the layout size of the RecyclerView
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(activity, numberOfColumns)
        }
    }

    private fun setupObservers() {
        overviewViewModel.challenges.observe(viewLifecycleOwner, Observer {
            it?.let {
recyclerAdapter.challengeList = it
            }
        })
    }

    private fun createDummyRecyclerList(): List<Challenge> {
        val challengeList = mutableListOf<Challenge>()

        val drawable: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.test, null)

        for (i in 1..10) {
            challengeList += Challenge(
                "$i",
                "Custom Challenge",
                "Custom Description",
                drawable,
                3,
                "medium",
                10f
            )
        }

        return challengeList
    }

    companion object {
        private const val DEFAULT_NUMBER_COLUMNS = 2
    }
}