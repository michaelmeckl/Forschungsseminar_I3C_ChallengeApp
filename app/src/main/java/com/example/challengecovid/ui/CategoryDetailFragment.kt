package com.example.challengecovid.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.challengecovid.R
import com.example.challengecovid.createShareIntent
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