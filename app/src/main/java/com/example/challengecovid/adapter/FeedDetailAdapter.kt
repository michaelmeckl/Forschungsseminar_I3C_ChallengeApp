package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.User
import kotlinx.android.synthetic.main.feed_detail_user_item.view.*

class FeedDetailAdapter : RecyclerView.Adapter<FeedDetailAdapter.FeedDetailViewHolder>() {

    /*
    companion object {
        private const val ITEM_VIEW_TYPE_HEADER = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }*/

    var participants = listOf<User>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = participants.size

    /*
    override fun getItemViewType(position: Int) = when (position) {
        0 -> ITEM_VIEW_TYPE_HEADER
        else -> ITEM_VIEW_TYPE_ITEM
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedDetailViewHolder {
        return FeedDetailViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FeedDetailViewHolder, position: Int) {
        holder.bind(participants[position])
    }

    /*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> FeedDetailViewHolder.from(parent)
            else -> error("Unhandled viewType=$viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val viewType = getItemViewType(position)) {
            ITEM_VIEW_TYPE_HEADER -> (holder as HeaderViewHolder)
            ITEM_VIEW_TYPE_ITEM -> (holder as FeedDetailViewHolder).bind(participants[position-1])
            else -> error("Unhandled viewType=$viewType")
        }
    }*/


    class FeedDetailViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: User) {
            itemView.user_name.text = data.username

            val userIcon = itemView.context.resources.getIdentifier(
                data.userIcon,
                "drawable",
                itemView.context.packageName
            )
            itemView.user_picture.setImageResource(userIcon)
        }

        companion object {
            fun from(parent: ViewGroup): FeedDetailViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.feed_detail_user_item, parent, false)

                return FeedDetailViewHolder(view)
            }
        }
    }

    /*
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.feed_detail_recycler_header, parent, false)
                return HeaderViewHolder(view)
            }
        }
    }*/
}