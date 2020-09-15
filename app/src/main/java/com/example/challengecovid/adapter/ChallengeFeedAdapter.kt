package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.model.UserChallenge
import kotlinx.android.synthetic.main.social_feed_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChallengeFeedAdapter(private val clickListener: ChallengeFeedClickListener) :
    ListAdapter<UserChallenge, ChallengeFeedAdapter.FeedViewHolder>(FeedDiffCallback()) {

    /*
    var publicChallenges = listOf<UserChallenge>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener = clickListener)
    }

    class FeedViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val userRepository = RepositoryController.getUserRepository()

        fun bind(data: UserChallenge, clickListener: ChallengeFeedClickListener) {
            itemView.feed_item_title.text = data.title
            if (data.description != "") {
                itemView.feed_item_description.text = data.description
            } else {
                itemView.feed_item_description.text = "Keine Beschreibung"
            }

            //FIXME: eigentlich sollte das im viewmodel passieren und nicht im Adapter
            // -> schlechte Performance und keine saubere Trennung von View und Logic! (aber funktioniert im Moment so)

            // get the creator of this challenge on the IO scope
            CoroutineScope(Dispatchers.IO).launch {
                val creator = userRepository.getUserOnce(data.creatorId)

                // switch to the Main thread to update the UI
                withContext(Dispatchers.Main) {
                    if (creator != null) {
                        // set icon and name of the user that created that challenge
                        itemView.feed_item_creator.text = itemView.resources.getString(R.string.creator_in_feed, creator.username)
                        itemView.feed_item_creator_level.text = "Level " + creator.level
                        val userIcon = itemView.context.resources.getIdentifier(
                            creator.userIcon,
                            "drawable",
                            itemView.context.packageName
                        )
                        itemView.feed_creator_icon.setImageResource(userIcon)

                    } else {
                        // set default userIcon and name
                        val textAnonymousUser = itemView.resources.getString(R.string.username_placeholder)
                        itemView.feed_item_creator.text =
                            itemView.resources.getString(R.string.creator_in_feed, textAnonymousUser)
                        itemView.feed_creator_icon.setImageResource(R.drawable.ic_person)
                    }
                }
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

// This class efficiently checks which items need to be updated so only these are redrawn and not the entire list!
class FeedDiffCallback : DiffUtil.ItemCallback<UserChallenge>() {
    override fun areItemsTheSame(oldItem: UserChallenge, newItem: UserChallenge): Boolean {
        return oldItem.challengeId == newItem.challengeId
    }

    override fun areContentsTheSame(oldItem: UserChallenge, newItem: UserChallenge): Boolean {
        return oldItem == newItem
    }
}

// ClickListener - Interface for the recycler view items
interface ChallengeFeedClickListener {
    fun onChallengeClick(challenge: UserChallenge)
}


