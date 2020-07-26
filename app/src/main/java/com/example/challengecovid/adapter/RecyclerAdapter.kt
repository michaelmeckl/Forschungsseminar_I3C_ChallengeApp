package com.example.challengecovid.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.CoronaStatistics
import kotlinx.android.synthetic.main.list_item_template.view.*

class RecyclerAdapter (private val statisticsList: List<CoronaStatistics>) : RecyclerView.Adapter<RecyclerAdapter.RecyclerListHolder>() {

    private lateinit var itemView: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerListHolder {
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_template, parent, false)
        return RecyclerListHolder(itemView)
    }

    override fun getItemCount() = statisticsList.size

    override fun onBindViewHolder(holder: RecyclerListHolder, position: Int) {
        holder.bindData(statisticsList[position])
    }

    class RecyclerListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            itemView.list_item.setBackgroundResource(R.drawable.card_view_ripple)
        }

        //TODO:
        fun bindData(data: CoronaStatistics) {
            itemView.item_title.text = "Test Title"

            val drawable: Drawable? = ResourcesCompat.getDrawable(itemView.resources, R.drawable.test, null)
            itemView.item_image.setImageDrawable(drawable)
            //itemView.item_image.setImageResource(R.drawable.ic_star)  // too memory intensive
        }
    }
}