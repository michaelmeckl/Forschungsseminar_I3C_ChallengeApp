package com.example.challengecovid.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.challengecovid.Constants
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.model.ChallengeType
import kotlinx.android.synthetic.main.fragment_challenge_detail.*
import timber.log.Timber
import kotlin.concurrent.thread


class ChallengeDetailFragment : Fragment() {

    // get the given navigation arguments lazily
    private val arguments: ChallengeDetailFragmentArgs by navArgs()
    private val challengeRepository = RepositoryController.getChallengeRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_challenge_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val (id, title, description, type, difficulty) = arguments

        challenge_detail_title.text = title
        challenge_detail_description.text = description
        challenge_detail_difficulty.text = difficulty

        if (type == ChallengeType.SYSTEM_CHALLENGE) {
            // hide the option to publish for system challenges
            publish_switch.visibility = View.GONE
        }

        // get the saved switch state and set it
        val sharedPrefs = activity?.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        val switchState = sharedPrefs?.getBoolean(Constants.PREFS_SWITCH_STATE + id, false) ?: false
        publish_switch.isChecked = switchState
        setStatus(switchState)

        publish_switch.setOnCheckedChangeListener { _, isChecked ->
            //update the public status of the challenge


//          TODO: Das funktioniert noch nicht, weil noch jedes mal false zurückgegeben wird :(
            thread {
                val isSuccess = challengeRepository.updatePublicStatus(id, publicStatus = isChecked)
                requireActivity().runOnUiThread {
                    if (isSuccess) {
                        Timber.d("Success updating challenge status")
                    } else {
                        Timber.d("Failure updating challenge status")
                    }
                }
            }
            sharedPrefs?.edit()?.putBoolean(Constants.PREFS_SWITCH_STATE + id, isChecked)?.apply()

//          TODO: Hier wird noch primitiv eine Progressbar für 2s eingeblendet und dann der jeweils andere state angezeigt, obwohl nicht geschaut wird ob success oder failure
            challenge_detail_progressbar.visibility = View.VISIBLE
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            Handler().postDelayed(this::switchStatus, 2000)

        }

    }

    private fun setStatus(switchState: Boolean) {
        if (switchState) {
            challenge_detail_relativelayout_offline.visibility = View.INVISIBLE
            challenge_detail_relativelayout_online.visibility = View.VISIBLE
        } else {
            challenge_detail_relativelayout_online.visibility = View.INVISIBLE
            challenge_detail_relativelayout_offline.visibility = View.VISIBLE
        }
    }

    private fun switchStatus() {
        if (challenge_detail_relativelayout_offline.visibility == View.INVISIBLE) {
            challenge_detail_relativelayout_online.visibility = View.INVISIBLE
            challenge_detail_relativelayout_offline.visibility = View.VISIBLE
        } else {
            challenge_detail_relativelayout_offline.visibility = View.INVISIBLE
            challenge_detail_relativelayout_online.visibility = View.VISIBLE
        }
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        challenge_detail_progressbar.visibility = View.GONE
    }

    // Create the Share Intent
    private fun getShareIntent(): Intent {
        val message = "Challenge:\n${arguments.title}\n${arguments.description}"

        // Create intent to show the chooser dialog
        return Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }, requireActivity().getString(R.string.share_title))
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
