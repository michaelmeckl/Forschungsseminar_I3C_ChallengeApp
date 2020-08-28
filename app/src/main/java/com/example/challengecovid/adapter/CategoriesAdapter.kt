package com.example.challengecovid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.challengecovid.R
import com.example.challengecovid.model.ChallengeCategory
import kotlinx.android.synthetic.main.category_list_item.view.*

//TODO: von : FirestoreAdapter<RatingAdapter.ViewHolder>(query) erben lassen?
/* like so:

    in onCreateView:

        adapter = object : RestaurantAdapter(query, this@MainActivity) {
            override fun onDataChanged() {
                // Show/hide content if the query returns empty.
                if (itemCount == 0) {
                    binding.recyclerRestaurants.visibility = View.GONE
                    binding.viewEmpty.visibility = View.VISIBLE
                } else {
                    binding.recyclerRestaurants.visibility = View.VISIBLE
                    binding.viewEmpty.visibility = View.GONE
                }
            }

            override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
                Snackbar.make(binding.root,
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show()
            }
        }


     in onStart: adapter.startListening()

     in onStop: adapter.stopListening()
 */
class CategoriesAdapter(private val clickListener: CategoryClickListener) :
    ListAdapter<ChallengeCategory, CategoriesAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.bind(item, clickListener)
    }

    // define the view holder with a private constructor so it can only be instantiated with the from()-Method
    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            data: ChallengeCategory,
            clickListener: CategoryClickListener
        ) {
            // set a ripple effect on Click
            itemView.category_item.setBackgroundResource(R.drawable.card_view_ripple)
            //itemView.list_item.setBackgroundColor(ResourcesCompat.getColor(itemView.context.resources, R.color.content_background, null))

            itemView.item_title.text = data.title
            //itemView.item_description.text = data.description
            val iconIdentifier =
                itemView.context.resources.getIdentifier(data.categoryIcon, "drawable", itemView.context.packageName)
            itemView.item_image.setImageResource(iconIdentifier)

            // set an unique transition name for each imageView
            itemView.item_image.transitionName = data.categoryIcon

            itemView.setOnClickListener {
                //call the interface method
                clickListener.onCategoryClick(it.item_image, data)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.category_list_item, parent, false)

                return ViewHolder(view)
            }
        }
    }
}

// This class efficiently checks which items need to be updated so only these are redrawn and not the entire list!
class DiffCallback : DiffUtil.ItemCallback<ChallengeCategory>() {
    override fun areItemsTheSame(oldItem: ChallengeCategory, newItem: ChallengeCategory): Boolean {
        return oldItem.title == newItem.title   // actually the id should be used, but since we use Firebase there is none
    }

    override fun areContentsTheSame(oldItem: ChallengeCategory, newItem: ChallengeCategory): Boolean {
        return oldItem == newItem
    }
}

// ClickListener - Interface for the recycler view items
interface CategoryClickListener {
    fun onCategoryClick(itemView: View, category: ChallengeCategory)
}