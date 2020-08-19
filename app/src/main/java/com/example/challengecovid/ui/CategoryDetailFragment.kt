package com.example.challengecovid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.challengecovid.R
import kotlinx.android.synthetic.main.fragment_category_detail.*


class CategoryDetailFragment: Fragment() {

    private val arguments : CategoryDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the shared element transition that should be performed when the view is created
        //sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.shared_element_transition)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.slide_top)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_category_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set the same transition name on the new image view to enable the shared element transition!
        detail_image.transitionName = resources.getString(R.string.shared_element_transition_name)
        detail_image.setImageResource(arguments.imageRes)
        detail_title.text = arguments.title
        detail_description.text = arguments.description
    }

    /*
    // TODO: sort of a "hack" for fullscreen dialog fragment
    // see https://stackoverflow.com/questions/12478520/how-to-set-dialogfragments-width-and-height
    override fun onResume() {
        // Get existing layout params for the window
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
        // Call super onResume after sizing
        super.onResume()
    }
     */
}