package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.Challenge
import kotlinx.android.synthetic.main.list_item_template.view.*

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private lateinit var itemView: View

    var challengeList = listOf<Challenge>()
        set(value) {
            field = value
            notifyDataSetChanged()  //TODO improve this, this redraws the whole list
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_template, parent, false)
        return ViewHolder(itemView)
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount() = challengeList.size

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindData(challengeList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            //save original elevation in a local variable to prevent the elevation from decreasing by half on every click
            val originalElevation = itemView.elevation

            // apply a "pressed" visual effect by decreasing elevation and showing a ripple effect
            itemView.setOnClickListener {
                itemView.elevation = originalElevation / 2
            }
            // set a ripple effect
            itemView.list_item.setBackgroundResource(R.drawable.card_view_ripple)
        }

        fun bindData(data: Challenge) {
            itemView.item_title.text = data.id + ". " + data.title
            //val drawable: Drawable? = ResourcesCompat.getDrawable(itemView.resources, R.drawable.test, null)
            itemView.item_image.setImageDrawable(data.image)
        }
    }
}