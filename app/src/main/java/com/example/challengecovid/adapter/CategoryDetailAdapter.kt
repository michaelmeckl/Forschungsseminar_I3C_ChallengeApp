package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.BaseChallenge
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.UserChallenge
import kotlinx.android.synthetic.main.category_detail_list_item.view.*

class CategoryDetailAdapter (private val clickListener: CategoryChallengeClickListener) :
    RecyclerView.Adapter<CategoryDetailAdapter.CategoryChallengeViewHolder>() {

    var categoryChallenges = listOf<Challenge>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var activeUserChallenges = setOf<BaseChallenge>()

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

            // disable the button if this challenge has already been accepted by the user
            //itemView.challenge_accept_button.isEnabled = !data.accepted

            // TODO: das reicht noch nicht, wenn gelöscht, ist sie wieder aktiv, das ist aber eigentlich nicht so toll...
            if(activeUserChallenges.contains(data)) {
                itemView.challenge_accept_button.isEnabled = false
            }

            //set an item click listener
            itemView.challenge_accept_button.setOnClickListener {
                clickListener.onCategoryChallengeClick(data)

                //TODO: or show an accepted button instead?
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