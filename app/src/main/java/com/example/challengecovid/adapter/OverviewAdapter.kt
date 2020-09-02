package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.Constants
import com.example.challengecovid.R
import com.example.challengecovid.model.BaseChallenge
import kotlinx.android.synthetic.main.challenge_item.view.*

class OverviewAdapter(private val clickListener: ChallengeClickListener, private val checkmarkClickListener: CheckmarkClickListener) :
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
        holder.bind(activeChallenges[position], clickListener, checkmarkClickListener)
    }

    override fun getItemCount() = activeChallenges.size

    class ChallengeViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: BaseChallenge, clickListener: ChallengeClickListener, checkmarkClickListener: CheckmarkClickListener) {
            itemView.name_challenge.text = data.title
            itemView.xp_challenge.text = String.format("%s XP", data.difficulty.points)
            itemView.description_challenge.text = data.description

            if (data.completed) {
                itemView.description_challenge.visibility = View.GONE
                itemView.description_challenge_completed.visibility = View.VISIBLE
                itemView.icon_challenge.setImageResource(R.drawable.ic_checkmark_checked)
            } else {
                itemView.description_challenge.visibility = View.VISIBLE
                itemView.description_challenge_completed.visibility = View.GONE
                itemView.icon_challenge.setImageResource(R.drawable.ic_checkmark_unchecked)
            }
            val sharedPrefs =
                itemView.context?.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
            val switchState = sharedPrefs?.getBoolean(Constants.PREFS_SWITCH_STATE + data.challengeId, false) ?: false

            if (switchState) {
                itemView.is_online_challenge.visibility = View.VISIBLE
            }

            //set challenge item click listener
            itemView.setOnClickListener {
                clickListener.onChallengeClick(data)
            }
            //set checkmark item click listener
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


