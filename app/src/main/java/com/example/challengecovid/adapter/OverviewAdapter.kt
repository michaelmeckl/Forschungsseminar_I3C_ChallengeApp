package com.example.challengecovid.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.BaseChallenge
import com.example.challengecovid.model.ChallengeType
import kotlinx.android.synthetic.main.challenge_item.view.*
import timber.log.Timber

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
            itemView.xp_challenge.text = String.format("%s XP", data.difficulty.points)
            itemView.description_challenge.text = data.description

            if(data.type == ChallengeType.USER_CHALLENGE) {
                itemView.name_challenge.text = data.title + " (Eigene Challenge)"
            } else {
                itemView.name_challenge.text = data.title
            }

            //TODO: im moment werden neue challenges auch sofort als grün markiert sobald eine als completed markiert ist!
            // ein ui update (z.B durch rotation) macht es wieder richtig ????
            // wenn neue dazu kommen funktionierts auch wieder nciht mehr (vllt genau wie bei accepted machen?)
            if (data.completed) {
                Timber.d("bind, userChallenge.completed = true")
                val cardView = itemView as? CardView ?: return

                cardView.setCardBackgroundColor(itemView.resources.getColor(R.color.colorPrimary, null))
                cardView.description_challenge.text = "Abgeschlossen"
                cardView.xp_challenge.visibility = View.INVISIBLE
                cardView.checkmark_completed_challenge.visibility = View.VISIBLE
            }

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


