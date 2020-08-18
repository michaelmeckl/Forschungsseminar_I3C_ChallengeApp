package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.ChallengeCategory
import kotlinx.android.synthetic.main.list_item_template.view.*

class RecyclerAdapter(private val clickListener: ChallengeClickListener) :
    ListAdapter<Challenge, RecyclerAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.bind(item, clickListener)
    }

    // define the view holder with a private constructor so it can only be instantiated with the from()-Method
    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            data: Challenge,
            clickListener: ChallengeClickListener
        ) {
            // set a ripple effect on Click
            itemView.list_item.setBackgroundResource(R.drawable.card_view_ripple)

            itemView.item_title.text = data.title
            itemView.item_description.text = data.description

            //itemView.list_item.setBackgroundColor(ResourcesCompat.getColor(itemView.context.resources, R.color.content_background, null))

            //val drawable: Drawable? = ResourcesCompat.getDrawable(itemView.context.resources, data.iconPath ?: return, null)
            //itemView.item_image.setImageDrawable(drawable)
            itemView.item_image.setImageResource(data.iconPath ?: return)
            /**
            //set the ImageView bounds to match the Drawable's dimensions
            adjustViewBounds = true
            layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
             */

            itemView.setOnClickListener {
                //call the interface method
                clickListener.onChallengeClick(data)
            }
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
        return oldItem.challengeId == newItem.challengeId
    }

    override fun areContentsTheSame(oldItem: Challenge, newItem: Challenge): Boolean {
        return oldItem == newItem
    }
}

// ClickListener - Interfaces for the recycler view items

interface CategoryClickListener {
    fun onCategoryClick(category: ChallengeCategory)
}

interface ChallengeClickListener {
    fun onChallengeClick(challenge: Challenge)
}