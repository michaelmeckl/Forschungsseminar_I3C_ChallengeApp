package com.example.challengecovid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.transition.TransitionInflater
import com.example.challengecovid.R


class CategoryDetailFragment: DialogFragment() {

    //private val args : DetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_category_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ViewCompat.setTransitionName(binding.videoTitle, "title_${args.videoId}")
        //imageId = args.uri
        //item_image.transitionName = imageId

        //mBinding.executePendingBindings()
    }

    /*
    // sort of a "hack" for fullscreen, but it works
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