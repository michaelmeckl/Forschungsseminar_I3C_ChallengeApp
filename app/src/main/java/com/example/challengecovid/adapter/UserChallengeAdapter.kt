package com.example.challengecovid.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.UserChallenge
import kotlinx.android.synthetic.main.challenge_item.view.*
import timber.log.Timber

class UserChallengeAdapter(private val clicklistener: UserChallengeOnClickListener) : RecyclerView.Adapter<UserChallengeAdapter.ChallengeViewHolder>() {


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
        holder.bind(userChallenges[position], clicklistener)
    }

    override fun getItemCount() = userChallenges.size

    class ChallengeViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: UserChallenge, clicklistener: UserChallengeOnClickListener) {
            itemView.name_challenge.text = data.title
            itemView.xp_challenge.text= String.format("%s XP", data.difficulty.points)
            itemView.description_challenge.text = data.description
            //itemView.icon_challenge.setImageResource(data.iconPath)   //TODO: statt icon vllt duration anzeigen oder difficulty?
            if (data.completed) {
                Timber.d("bind, data.completed = true")
                val cardView = itemView as CardView

                cardView.setCardBackgroundColor(Color.parseColor("#A1E887"))
                cardView.description_challenge.text = "Heute Abgeschlossen"
                cardView.xp_challenge.visibility = View.INVISIBLE
                cardView.checkmark_completed_challenge.visibility = View.VISIBLE
            }

            itemView.setOnClickListener{
                clicklistener.onItemClick(data)
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
    interface UserChallengeOnClickListener {
        fun onItemClick(userChallenge: UserChallenge)
    }

}


