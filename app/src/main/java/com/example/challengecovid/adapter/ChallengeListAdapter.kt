package com.example.challengecovid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.Challenge

class ChallengeListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<ChallengeListAdapter.ChallengeViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var challenges = emptyList<Challenge>() // Cached copy of words

    inner class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val challengeItemView: TextView = itemView.findViewById(R.id.name_challenge)
        val challengeItemXP: TextView = itemView.findViewById(R.id.xp_challenge)
        val challengeItemIcon: ImageView = itemView.findViewById(R.id.icon_challenge)
        val challengeItemDescription: TextView = itemView.findViewById(R.id.description_challenge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val itemView = inflater.inflate(R.layout.challenge_item, parent, false)
        return ChallengeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val current = challenges[position]
        holder.challengeItemView.text = current.title
        holder.challengeItemXP.text = String.format("%s XP", current.points.toString())
        holder.challengeItemDescription.text = current.description
//        TODO: Sollte so gehen später
//        holder.challengeItemIcon.background = current.iconPath
    }

    internal fun setChallenges(challenges: List<Challenge>) {
        this.challenges = challenges
        notifyDataSetChanged()
    }

    override fun getItemCount() = challenges.size

    fun getChallengeAt(position: Int): Challenge {
        return challenges[position]
    }
}


