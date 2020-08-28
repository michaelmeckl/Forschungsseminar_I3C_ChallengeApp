package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.model.UserChallenge
import kotlinx.android.synthetic.main.social_feed_item.view.*

class ChallengeFeedAdapter : RecyclerView.Adapter<ChallengeFeedAdapter.FeedViewHolder>() {

    var publicChallenges = listOf<UserChallenge>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun getChallengeAt(position: Int): UserChallenge {
        return publicChallenges[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(publicChallenges[position])
    }

    override fun getItemCount() = publicChallenges.size

    class FeedViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val userRepository = RepositoryController.getUserRepository()

        fun bind(data: UserChallenge) {
            itemView.feed_item_title.text = data.title
            itemView.feed_item_description.text = data.description

            // get the creator of this challenge
            val creator = userRepository.getUser(data.creatorId)
            if (creator != null) {
                // set icon and name of the user that created that challenge
                itemView.feed_item_creator.text = creator.username
                val userIcon =
                    itemView.context.resources.getIdentifier(creator.userIcon, "drawable", itemView.context.packageName)
                itemView.feed_creator_icon.setImageResource(userIcon)

            } else {
                // set default userIcon and name
                itemView.feed_item_creator.text = "Anonym"
                itemView.feed_creator_icon.setImageResource(R.drawable.ic_person)
            }
        }

        companion object {
            fun from(parent: ViewGroup): FeedViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.social_feed_item, parent, false)

                return FeedViewHolder(view)
            }
        }
    }
}


