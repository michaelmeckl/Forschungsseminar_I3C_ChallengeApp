package com.example.challengecovid.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.BaseChallenge
import kotlinx.android.synthetic.main.challenge_item.view.*
import timber.log.Timber

class OverviewAdapter(private val clickListener: ChallengeClickListener, private val checkmarkClickListener: CheckmarkClickListener) :
    RecyclerView.Adapter<OverviewAdapter.ChallengeViewHolder>() {

    var activeChallenges = listOf<BaseChallenge>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
//  Das fixt den Anzeigefehler der Challenges entgültig. Ist nicht wirklich die beste Lösung
    override fun onViewAttachedToWindow(holder: ChallengeViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.setIsRecyclable(false)
    }

    fun getChallengeAt(position: Int): BaseChallenge {
        return activeChallenges[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        return ChallengeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bind(activeChallenges[position], clickListener, checkmarkClickListener)
    }

    override fun getItemCount() = activeChallenges.size

    class ChallengeViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: BaseChallenge, clickListener: ChallengeClickListener, checkmarkClickListener: CheckmarkClickListener) {
            itemView.name_challenge.text = data.title
            itemView.xp_challenge.text = String.format("%s XP", data.difficulty.points)
            itemView.description_challenge.text = data.description
//            itemView.icon_challenge.setImageResource(R.drawable.ic_checkmark_unchecked)   //TODO: statt icon vllt duration anzeigen oder difficulty?

            //TODO: im moment werden neue challenges auch sofort als grün markiert sobald eine als completed markiert ist!
            // ein ui update (z.B durch rotation) macht es wieder richtig ????
            if (data.completed) {
                Timber.d("bind, userChallenge.completed = true")
                val cardView = itemView as? CardView ?: return

//                TODO: Also wenn die Zeile auskommentiert ist, scheint der Bug komplett weg zu sein. Entweder wir lassen die grüne Hintergrundfarbe weg oder ich find was wies anders gemacht werden kann
//                Edit: nvm, die xp werden trotzdem noch manchmal nicht angezeigt
//                cardView.setCardBackgroundColor(Color.parseColor("#A1E887"))
                cardView.description_challenge.text = "Heute Abgeschlossen"
                cardView.xp_challenge.visibility = View.INVISIBLE
                cardView.icon_challenge.setImageResource(R.drawable.ic_checkmark_checked)

            } else {
                itemView.icon_challenge.setImageResource(R.drawable.ic_checkmark_unchecked)
            }

            //set an item click listener
            itemView.setOnClickListener {
                clickListener.onChallengeClick(data)
            }

            itemView.icon_challenge.setOnClickListener {
                checkmarkClickListener.onCheckmarkClick(data)
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
interface CheckmarkClickListener {
    fun onCheckmarkClick(challenge: BaseChallenge)
}


