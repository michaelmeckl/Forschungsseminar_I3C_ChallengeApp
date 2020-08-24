package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.UserChallenge
import kotlinx.android.synthetic.main.challenge_item.view.*

class UserChallengeAdapter : RecyclerView.Adapter<UserChallengeAdapter.ChallengeViewHolder>() {

    var userChallenges = listOf<UserChallenge>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun getChallengeAt(position: Int): UserChallenge {
        return userChallenges[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        return ChallengeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bind(userChallenges[position])
    }

    override fun getItemCount() = userChallenges.size

    class ChallengeViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: UserChallenge) {
            itemView.name_challenge.text = data.title
            itemView.xp_challenge.text= String.format("%s XP", data.difficulty.points)
            itemView.description_challenge.text = data.description
            //itemView.icon_challenge.setImageResource(data.iconPath)   //TODO: statt icon vllt duration anzeigen oder difficulty?
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


