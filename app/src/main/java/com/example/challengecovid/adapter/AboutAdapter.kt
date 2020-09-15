package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.ui.AttributionItem
import kotlinx.android.synthetic.main.about_list_item.view.*

class AboutAdapter : RecyclerView.Adapter<AboutAdapter.ViewHolder>() {

    var attributionList = listOf<AttributionItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount() = attributionList.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(attributionList[position])
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: AttributionItem) {
            itemView.about_image.setImageResource(data.imageResource)
            itemView.about_caption.text = data.attribution
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.about_list_item, parent, false)

                return ViewHolder(view)
            }
        }
    }
}