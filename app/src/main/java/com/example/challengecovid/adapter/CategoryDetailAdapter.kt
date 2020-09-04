package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.BaseChallenge
import com.example.challengecovid.model.Challenge
import kotlinx.android.synthetic.main.category_detail_list_item.view.*

class CategoryDetailAdapter (private val clickListener: CategoryChallengeClickListener) :
    RecyclerView.Adapter<CategoryDetailAdapter.CategoryChallengeViewHolder>() {

    var categoryChallenges = listOf<Challenge>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var activeUserChallenges = setOf<BaseChallenge>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryChallengeViewHolder {
        return CategoryChallengeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CategoryChallengeViewHolder, position: Int) {
        holder.bind(categoryChallenges[position], activeUserChallenges, clickListener)
    }

    override fun getItemCount() = categoryChallenges.size

    class CategoryChallengeViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: Challenge, activeUserChallenges: Set<BaseChallenge>, clickListener: CategoryChallengeClickListener) {
            itemView.challenge_title.text = data.title
            itemView.challenge_xp.text = String.format("%s XP", data.difficulty.points)
            itemView.challenge_difficulty.text = itemView.resources.getString(R.string.difficulty, data.difficulty.toString())
            itemView.challenge_description.text = data.description

            // disable the button if this challenge has already been accepted by the user
            //itemView.challenge_accept_button.isEnabled = !data.accepted

            // contains reicht hier nicht, da sich die anderen Properties (außer der ID) verändern können
            for (challenge in activeUserChallenges) {
                if (challenge.challengeId == data.challengeId) {
                    itemView.challenge_accept_button.isEnabled = false
                    return
                }
            }

            //set an item click listener
            itemView.challenge_accept_button.setOnClickListener {
                clickListener.onCategoryChallengeClick(data)

                //gray out button and disable it
                itemView.challenge_accept_button.isEnabled = false
            }
        }

        companion object {
            fun from(parent: ViewGroup): CategoryChallengeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.category_detail_list_item, parent, false)

                return CategoryChallengeViewHolder(view)
            }
        }
    }
}

// ClickListener - Interface for the recycler view items
interface CategoryChallengeClickListener {
    fun onCategoryChallengeClick(challenge: Challenge)
}