package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.BaseChallenge
import kotlinx.android.synthetic.main.challenge_item.view.*

class OverviewAdapter(private val clickListener: ChallengeClickListener) :
    RecyclerView.Adapter<OverviewAdapter.ChallengeViewHolder>() {

    var activeChallenges = listOf<BaseChallenge>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun getChallengeAt(position: Int): BaseChallenge {
        return activeChallenges[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        return ChallengeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bind(activeChallenges[position], clickListener)
    }

    override fun getItemCount() = activeChallenges.size

    class ChallengeViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: BaseChallenge, clickListener: ChallengeClickListener) {
            itemView.name_challenge.text = data.title
            itemView.xp_challenge.text = String.format("%s XP", data.difficulty.points)
            itemView.description_challenge.text = data.description
            //itemView.icon_challenge.setImageResource(data.iconPath)   //TODO: statt icon vllt duration anzeigen oder difficulty?

            //set an item click listener
            itemView.setOnClickListener {
                clickListener.onChallengeClick(data)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ChallengeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.challenge_item, parent, false)

                return ChallengeViewHolder(view)
            }
        }
    }
}

// ClickListener - Interface for the recycler view items
interface ChallengeClickListener {
    fun onChallengeClick(challenge: BaseChallenge)
}


