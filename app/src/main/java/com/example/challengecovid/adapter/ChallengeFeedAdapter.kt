package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

            //FIXME: eigentlich sollte das im viewmodel passieren und nicht im Adapter
            // -> schlechte Performance und keine saubere Trennung von View und Logic!

            //TODO: die liste blinkt ständig wenn neue items geladen werden! -> nur einmal laden?

            // get the creator of this challenge on the IO scope
            CoroutineScope(Dispatchers.IO).launch {
                val creator = userRepository.getUserOnce(data.creatorId)

                // switch to the Main thread to update the UI
                withContext(Dispatchers.Main) {
                    if (creator != null) {
                        // set icon and name of the user that created that challenge
                        itemView.feed_item_creator.text = creator.username
                        val userIcon = itemView.context.resources.getIdentifier(
                            creator.userIcon,
                            "drawable",
                            itemView.context.packageName
                        )
                        itemView.feed_creator_icon.setImageResource(userIcon)

                    } else {
                        // set default userIcon and name
                        itemView.feed_item_creator.text = itemView.resources.getString(R.string.username_placeholder)
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

// ClickListener - Interface for the recycler view items
interface ChallengeFeedClickListener {
    fun onChallengeClick(challenge: UserChallenge)
}


