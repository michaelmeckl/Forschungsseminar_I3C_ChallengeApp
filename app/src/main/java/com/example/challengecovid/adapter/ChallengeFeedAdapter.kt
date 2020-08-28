package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.model.User
import com.example.challengecovid.model.UserChallenge
import kotlinx.android.synthetic.main.social_feed_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ChallengeFeedAdapter(private val clickListener: ChallengeFeedClickListener) :
    RecyclerView.Adapter<ChallengeFeedAdapter.FeedViewHolder>() {

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
        holder.bind(publicChallenges[position], clickListener = clickListener)
    }

    override fun getItemCount() = publicChallenges.size

    class FeedViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val userRepository = RepositoryController.getUserRepository()

        fun bind(data: UserChallenge, clickListener: ChallengeFeedClickListener) {
            itemView.feed_item_title.text = data.title
            itemView.feed_item_description.text = data.description

            // get the creator of this challenge
            var creator: User? = null

            //TODO: this doesn't show an image right now!
            CoroutineScope(Dispatchers.IO).launch {
                val user = userRepository.getUser(data.creatorId)
                creator = user
            }

            if (creator != null) {
                // set icon and name of the user that created that challenge
                itemView.feed_item_creator.text = creator?.username
                val userIcon = itemView.context.resources.getIdentifier(creator?.userIcon, "drawable", itemView.context.packageName)
                itemView.feed_creator_icon.setImageResource(userIcon)

            } else {
                // set default userIcon and name
                itemView.feed_item_creator.text = "Anonym"
                itemView.feed_creator_icon.setImageResource(R.drawable.ic_person)
            }

            //set an item click listener
            itemView.setOnClickListener {
                clickListener.onChallengeClick(data)
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

// ClickListener - Interface for the recycler view items
interface ChallengeFeedClickListener {
    fun onChallengeClick(challenge: UserChallenge)
}


