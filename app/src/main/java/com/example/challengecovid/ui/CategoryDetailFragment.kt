package com.example.challengecovid.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.challengecovid.Constants
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.adapter.CategoryChallengeClickListener
import com.example.challengecovid.adapter.CategoryDetailAdapter
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.viewmodels.CategoryDetailViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.fragment_category_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryDetailFragment : Fragment() {

    // get the given navigation arguments lazily
    private val arguments: CategoryDetailFragmentArgs by navArgs()

    private lateinit var categoryDetailAdapter: CategoryDetailAdapter
    private lateinit var categoryDetailViewModel: CategoryDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the shared element transition that should be performed when the view is created
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.shared_element_enter_transition)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_category_detail, container, false)

        val categoryRepository = RepositoryController.getCategoryRepository()
        val userRepository = RepositoryController.getUserRepository()
        val application = requireNotNull(this.activity).application
        categoryDetailViewModel = getViewModel { CategoryDetailViewModel(categoryRepository, userRepository, application) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val (id, _, description, imageName) = arguments

        // set the same transition name on the new image view to enable the shared element transition!
        detail_image.transitionName = imageName

        val imageIdentifier = requireActivity().resources.getIdentifier(imageName, "drawable", activity?.packageName)

        detail_image.setImageResource(imageIdentifier)
        detail_description.text = description

        showChallenges(id)
    }

    //TODO: die könnten auch vorgeladen und gecached werden theoretisch (für bessere Performance)
    private fun showChallenges(categoryId: String) {

        categoryDetailAdapter = CategoryDetailAdapter(object : CategoryChallengeClickListener {
            override fun onCategoryChallengeClick(challenge: Challenge) {
                acceptChallenge(challenge, categoryId)
            }
        })

        CoroutineScope(Dispatchers.Main).launch {
            val sharedPrefs = requireActivity().getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
            val userID = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""

            if(userID == "") return@launch

            // get all active challenges for the user and also those System_Challenges that have been accepted and deleted (= hidden)
            val challenges = categoryDetailViewModel.getActiveAndHiddenChallenges(userID) ?: return@launch
            categoryDetailAdapter.activeUserChallenges = challenges.toSet()
        }

        // fetch the challenges asynchronously from firebase and set them to the adapter afterwards
        CoroutineScope(Dispatchers.Main).launch {
            val challenges = categoryDetailViewModel.getChallengesForCategory(categoryId) ?: return@launch
            categoryDetailAdapter.categoryChallenges  = challenges
        }

        recycler_category_detail_list.apply {
            setHasFixedSize(true)
            adapter = categoryDetailAdapter
        }
    }

    private fun acceptChallenge(challenge: Challenge, categoryId: String) {
        val sharedPrefs = requireActivity().getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""
        
        if (userId == "") {
            val toast = Toast.makeText(requireActivity(), R.string.wrong_user_id_error, Toast.LENGTH_SHORT)
            toast.view.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
            toast.view.setPadding(8, 4, 8, 4)
            toast.show()

            return
        }

        categoryDetailViewModel.addToActiveChallenges(categoryId, challenge, userId)
        /*
        //TODO: this is really annoying when taking on multiple challenges
        val snackbar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),   // uses the android content to attach to
            "Challenge erfolgreich angenommen!",
            Snackbar.LENGTH_SHORT
        )
        snackbar.view.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
        snackbar.show()
        */
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()    // don't show the main menu in this fragment!
    }


    //add this to top: EventListener<DocumentSnapshot> to receive updates for the current category!
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