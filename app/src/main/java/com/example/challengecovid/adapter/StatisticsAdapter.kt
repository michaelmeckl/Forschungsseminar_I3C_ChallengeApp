package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.CoronaStatistics
import kotlinx.android.synthetic.main.statistics_list_item.view.*

class StatisticsAdapter (private val statisticsList: List<CoronaStatistics>) : RecyclerView.Adapter<StatisticsAdapter.StatisticsListHolder>() {

    private lateinit var itemView: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsListHolder {
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.statistics_list_item, parent, false)
        return StatisticsListHolder(itemView)
    }

    override fun getItemCount() = statisticsList.size

    override fun onBindViewHolder(holder: StatisticsListHolder, position: Int) {
        holder.bindData(statisticsList[position])
    }

    class StatisticsListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: maybe delete this if a click has no effect as this might confuse users?
        // apply a "pressed" effect by decreasing elevation and showing a ripple effect
        init {
            //save original elevation of the layout in a local variable as otherwise the elevation would decrease by half on every click
            val originalElevation = itemView.elevation

            // Define click listener for the ViewHolder's View.
            itemView.setOnClickListener {
                itemView.elevation = originalElevation / 2
            }
            // set a ripple effect
            itemView.statistics_list_item.setBackgroundResource(R.drawable.card_view_ripple)
        }

        //TODO:
        fun bindData(data: CoronaStatistics) {
            itemView.statistics_item_title.text = "New cases today:"
            itemView.statistics_item_count.text = data.casesToday.toString()
        }
    }
}