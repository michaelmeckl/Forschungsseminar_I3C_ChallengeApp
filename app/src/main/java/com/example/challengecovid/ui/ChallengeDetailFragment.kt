package com.example.challengecovid.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.challengecovid.R
import kotlinx.android.synthetic.main.fragment_challenge_detail.*

class ChallengeDetailFragment: Fragment() {

    // get the given navigation arguments lazily
    private val arguments: ChallengeDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_challenge_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val (title, description) = arguments

        challenge_detail_title.text = title
        challenge_detail_description.text = description

        publish_switch.setOnClickListener {

        }

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
}
