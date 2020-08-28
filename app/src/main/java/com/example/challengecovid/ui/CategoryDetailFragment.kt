package com.example.challengecovid.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.challengecovid.R
import kotlinx.android.synthetic.main.fragment_category_detail.*

class CategoryDetailFragment : Fragment() {

    // get the given navigation arguments lazily
    private val arguments: CategoryDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the shared element transition that should be performed when the view is created
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.shared_element_enter_transition)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_category_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val (_, description, imageName) = arguments

        // set the same transition name on the new image view to enable the shared element transition!
        detail_image.transitionName = imageName

        val imageIdentifier = requireActivity().resources.getIdentifier(imageName, "drawable", activity?.packageName)

        detail_image.setImageResource(imageIdentifier)
        detail_description.text = description
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()    // don't show the main menu in this fragment!
    }


    //TODO: add this to top: EventListener<DocumentSnapshot>
    /**
     * Listener for the Restaurant document ([.restaurantRef]).
     */
    /*
    override fun onEvent(snapshot: DocumentSnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) {
            Log.w(TAG, "restaurant:onEvent", e)
            return
        }

        snapshot?.let {
            val restaurant = snapshot.toObject<Restaurant>()
            if (restaurant != null) {
                onRestaurantLoaded(restaurant)
            }
        }
    }

    private fun onRestaurantLoaded(restaurant: Restaurant) {
        binding.restaurantName.text = restaurant.name
        binding.restaurantRating.rating = restaurant.avgRating.toFloat()
        binding.restaurantNumRatings.text = getString(R.string.fmt_num_ratings, restaurant.numRatings)
        binding.restaurantCity.text = restaurant.city
        binding.restaurantCategory.text = restaurant.category
        binding.restaurantPrice.text = RestaurantUtil.getPriceString(restaurant)

        // Background image
        Glide.with(binding.restaurantImage.context)
            .load(restaurant.photo)
            .into(binding.restaurantImage)
    }
    */
}