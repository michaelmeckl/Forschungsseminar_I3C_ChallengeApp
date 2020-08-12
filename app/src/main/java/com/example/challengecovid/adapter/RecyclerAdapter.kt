package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.Challenge
import kotlinx.android.synthetic.main.list_item_template.view.*

class RecyclerAdapter : ListAdapter<Challenge, RecyclerAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.bind(item)
    }

    // define the view holder with a private constructor so it can only be instantiated with the from()-Method
    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            //save original elevation in a local variable to prevent the elevation from decreasing by half on every click
            val originalElevation = itemView.elevation

            // apply a "pressed" visual effect by decreasing elevation and showing a ripple effect
            itemView.setOnClickListener {
                itemView.elevation = originalElevation / 2
            }
            // set a ripple effect
            itemView.list_item.setBackgroundResource(R.drawable.card_view_ripple)
        }

        fun bind(data: Challenge) {
            itemView.item_title.text = data.id + ". " + data.title
            //val drawable: Drawable? = ResourcesCompat.getDrawable(itemView.context.resources, R.drawable.test, null)
            itemView.item_image.setImageDrawable(data.image)
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_template, parent, false)

                return ViewHolder(view)
            }
        }
    }
}

// This class efficiently checks which items need to be updated so only these are redrawn and not the entire list!
class DiffCallback : DiffUtil.ItemCallback<Challenge>() {
    override fun areItemsTheSame(oldItem: Challenge, newItem: Challenge): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Challenge, newItem: Challenge): Boolean {
        return oldItem == newItem
    }
}