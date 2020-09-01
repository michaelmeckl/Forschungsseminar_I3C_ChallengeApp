package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.Challenge
import kotlinx.android.synthetic.main.category_detail_list_item.view.*

class CategoryDetailAdapter (private val clickListener: CategoryChallengeClickListener) :
    RecyclerView.Adapter<CategoryDetailAdapter.CategoryChallengeViewHolder>() {

    var categoryChallenges = listOf<Challenge>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryChallengeViewHolder {
        return CategoryChallengeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CategoryChallengeViewHolder, position: Int) {
        holder.bind(categoryChallenges[position], clickListener)
    }

    override fun getItemCount() = categoryChallenges.size

    class CategoryChallengeViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: Challenge, clickListener: CategoryChallengeClickListener) {
            itemView.challenge_title.text = data.title
            itemView.challenge_xp.text = String.format("%s XP", data.difficulty.points)
            itemView.challenge_difficulty.text = itemView.resources.getString(R.string.difficulty, data.difficulty.toString())
            itemView.challenge_description.text = data.description

            //TODO: im moment werden neue challenges auch sofort als grün markiert sobald eine als completed markiert ist!
            // ein ui update (z.B durch rotation) macht es wieder richtig ????
            /*
            if (data.completed) {
                Timber.d("bind, userChallenge.completed = true")
                val cardView = itemView as? CardView ?: return

                cardView.setCardBackgroundColor(Color.parseColor("#A1E887"))
                cardView.description_challenge.text = "Heute Abgeschlossen"
                cardView.xp_challenge.visibility = View.INVISIBLE
                cardView.checkmark_completed_challenge.visibility = View.VISIBLE
            }*/

            //TODO: add property isAccepted boolean to check here!

            //set an item click listener
            itemView.challenge_accept_button.setOnClickListener {
                clickListener.onCategoryChallengeClick(data)

                //TODO funktioniert das für alle? oder doch lieber schauen ob in active challenges des aktuellen nutzers?
                data.accepted = true

                //TODO: or show accepted button?

                //gray out button and disable it
                itemView.challenge_accept_button.isEnabled = false

                /*
                itemView.challenge_accept_button.setBackgroundColor(itemView.resources.getColor(android.R.color.darker_gray, null))
                itemView.challenge_accept_button.setTextColor(itemView.resources.getColor(R.color.browser_actions_bg_grey, null))
                // bessere farben selber machen
                 */
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