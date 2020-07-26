package com.example.challengecovid.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.challengecovid.R
import com.example.challengecovid.Utils
import com.example.challengecovid.adapter.RecyclerAdapter
import com.example.challengecovid.model.CoronaStatistics
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.list_item_template.*

class OverviewFragment : Fragment() {

    private val overviewViewModel: OverviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_overview, container, false)

        //TODO: check if connection is available maybe in on start / on create of main activity instead? or in a separate networking class
        /*
        Utils.hasInternetConnection().subscribe { hasInternet ->
            println("Internet Access: $hasInternet")
            Toast.makeText(
                activity,
                "Has internet connection: $hasInternet",
                Toast.LENGTH_SHORT
            ).show()

         */

        setupObservers()

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
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerList = createDummyRecyclerList() //TODO: this should come from an api call later
        val recyclerAdapter = RecyclerAdapter(recyclerList)

        // calculate the number of columns used for the grid or the default number if this fragment has no context
        val numberOfColumns =
            this.context?.let { Utils.calculateNumberOfColumns(it) } ?: DEFAULT_NUMBER_COLUMNS

        list.apply {
            setHasFixedSize(true)
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(activity, numberOfColumns)
        }
    }

    private fun setupObservers() {
        overviewViewModel.statistics.observe(viewLifecycleOwner, Observer {
            // TODO: update recyclerAdapter
            item_title.text = it.casesToday.toString()
        })
    }

    /*
    fun fetchCurrentStatistics(country: String) {
        // deserialize objects with custom deserializer
        Fuel.get("https://disease.sh/v2/countries/${country}")
            .responseObject(CoronaStatistics.Deserializer()) { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        // Failed request
                        val ex = result.getException()
                        println(ex)
                    }
                    is Result.Success -> {
                        // Successful request
                        val data = result.get()
                        overviewViewModel.statistics.value = data
                    }
                }
            }
    }*/

    private fun createDummyRecyclerList(): List<CoronaStatistics> {
        val coronaStatisticsList = mutableListOf<CoronaStatistics>()

        for (i in 1..10) {
            coronaStatisticsList += CoronaStatistics(
                "Deutschland",
                34,
                12,
                10,
                3,
                24,
                10,
                12,
                80_000_000,
                null
            )
        }

        return coronaStatisticsList
    }

    companion object {
        private const val DEFAULT_NUMBER_COLUMNS = 2
    }
}