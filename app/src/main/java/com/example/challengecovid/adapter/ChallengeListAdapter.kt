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
import com.example.challengecovid.model.UserChallenge
import com.example.challengecovid.ui.CreditsItem

class ChallengeListAdapter internal constructor(
    private val context: Context
) : RecyclerView.Adapter<ChallengeListAdapter.ChallengeViewHolder>() {

    var challenges = listOf<UserChallenge>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val challengeItemView: TextView = itemView.findViewById(R.id.name_challenge)
        val challengeItemXP: TextView = itemView.findViewById(R.id.xp_challenge)
        val challengeItemIcon: ImageView = itemView.findViewById(R.id.icon_challenge)
        val challengeItemDescription: TextView = itemView.findViewById(R.id.description_challenge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.challenge_item, parent, false)
        return ChallengeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val current = challenges[position]
        holder.challengeItemView.text = current.title
        holder.challengeItemXP.text = String.format("%s XP", current.difficulty.points)
        holder.challengeItemDescription.text = current.description
//        TODO: Sollte so gehen später
//        holder.challengeItemIcon.background = current.iconPath
    }

    override fun getItemCount() = challenges.size

    fun getChallengeAt(position: Int): UserChallenge {
        return challenges[position]
    }
}


