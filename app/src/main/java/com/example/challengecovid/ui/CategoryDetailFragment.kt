package com.example.challengecovid.ui

import android.content.Intent
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

    // Create the Share Intent
    private fun getShareIntent() : Intent {
        val message = "Challenge:\n${arguments.title}\n\n${arguments.description}"

        // Create intent to show the chooser dialog
        return Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        },  requireActivity().getString(R.string.share_title))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()    // don't show the main menu in this fragment!
        inflater.inflate(R.menu.share_menu, menu)   // instead show a custom menu here

        // decide dynamically if the share icon should be shown by checking if the activity resolves
        if (null == getShareIntent().resolveActivity(requireActivity().packageManager)) {
            // hide the menu item if it doesn't resolve
            menu.findItem(R.id.action_share)?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                startActivity(getShareIntent())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

    /*
    // Helper function for calling a share functionality.
    // Should be used when user presses a share button/menu item.
    fun createShareIntent(context: Activity) {

        //val contenturi = Uri.parse("android.resource://" + context.packageName + "/drawable/" + "test")

        val shareIntent = ShareCompat.IntentBuilder.from(context)
            .setChooserTitle("Share via")
            //.setStream(contenturi)
            //.setType(image/**/)
            .setText("Click this: http://www.example.com/detail")
            .setType("text/plain")
            .createChooserIntent()
            .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        startActivity(context, shareIntent, null)
    }
    */

    /*
    private fun startEnterTransitionAfterLoadingImage(imageAddress: Int, imageView: ImageView) {
        Glide.with(this)
            .load(imageAddress)
            .circleCrop()
            //.dontTransform()
            .dontAnimate()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: com.bumptech.glide.request.target.Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }
            })
            .into(imageView)
    }

     */
}