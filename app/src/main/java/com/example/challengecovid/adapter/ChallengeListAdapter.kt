package com.example.challengecovid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val itemView = inflater.inflate(R.layout.challenge_item, parent, false)
        return ChallengeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val current = challenges[position]
        holder.challengeItemView.text = current.title
    }

    internal fun setChallenges(challenges: List<Challenge>) {
        this.challenges = challenges
        notifyDataSetChanged()
    }

    override fun getItemCount() = challenges.size
}


