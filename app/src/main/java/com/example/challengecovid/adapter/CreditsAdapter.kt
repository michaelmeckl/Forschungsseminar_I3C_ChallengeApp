package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.ChallengeCategory
import com.example.challengecovid.ui.CreditsFragment
import com.example.challengecovid.ui.CreditsItem
import kotlinx.android.synthetic.main.category_list_item.view.*
import kotlinx.android.synthetic.main.credits_list_item.view.*
import timber.log.Timber

class CreditsAdapter : RecyclerView.Adapter<CreditsAdapter.ViewHolder>() {

    var creditsList = listOf<CreditsItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount() = creditsList.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(creditsList[position])
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: CreditsItem) {
            itemView.credits_image.setImageResource(data.imageResource)
            itemView.credits_caption.text = data.attribution
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.credits_list_item, parent, false)

                return ViewHolder(view)
            }
        }
    }
}