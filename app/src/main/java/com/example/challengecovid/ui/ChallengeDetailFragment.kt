package com.example.challengecovid.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.challengecovid.Constants
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.model.ChallengeType
import com.example.challengecovid.model.Difficulty
import com.example.challengecovid.model.UserChallenge
import com.example.challengecovid.viewmodels.OverviewViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.fragment_challenge_detail.*
import timber.log.Timber

//FIXME: wenn eine challenge von anderen aus dem feed angenommen wird, kann man sie zwar nicht bearbeiten oder erneut veröffentlichen, das liegt aber daran dass sie automatisch als completed markiert ist sobald angenommen ????

// TODO -> aber nicht immer? manchmal gehts auch? ich bin verwirrt ... -> muss trotzdem gefixt werden
class ChallengeDetailFragment : Fragment() {

    // get the given navigation arguments lazily
    private val arguments: ChallengeDetailFragmentArgs by navArgs()

    private lateinit var overviewViewModel: OverviewViewModel

    private lateinit var sharedPrefs: SharedPreferences

    // current challenge properties
    private lateinit var id: String
    private lateinit var title: String
    private lateinit var description: String
    private lateinit var difficulty: String
    private lateinit var type: ChallengeType
    private var completed: Boolean = false

    private var isEditable: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val userRepository = RepositoryController.getUserRepository()
        val challengeRepository = RepositoryController.getChallengeRepository()
        val application = requireNotNull(this.activity).application
        overviewViewModel = getViewModel { OverviewViewModel(challengeRepository, userRepository, application) }

        return inflater.inflate(R.layout.fragment_challenge_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        id = arguments.id
        title = arguments.title
        description = arguments.description
        difficulty = arguments.difficulty
        type = arguments.type
        completed = arguments.completed

        setupInitialUI()

        isEditable = sharedPrefs.getBoolean(Constants.PREFS_IS_CHALLENGE_BY_THIS_USER + id, false)

        //TODO: statt alles ständig auf hidden oder visible zu setzen wäre hier wahrscheinlich ein neues eigenes Layout besser
        if (type == ChallengeType.SYSTEM_CHALLENGE || !isEditable) {
            // hide the option to publish and to edit for system challenges
            setVisibility(
                switch_layout,
                challenge_detail_start_editing,
                challenge_detail_relativelayout_online,
                challenge_detail_relativelayout_offline,
                visible = false
            )
        }

        challenge_detail_start_editing.setOnClickListener {

            setVisibility(
                challenge_detail_title,
                challenge_detail_description,
                challenge_detail_difficulty,
                challenge_detail_start_editing,
                switch_layout,
                visible = false
            )
            setVisibility(
                challenge_detail_header_edit,
                challenge_detail_stop_editing,
                layout_challenge_detail_title_edit,
                layout_challenge_detail_description_edit,
                visible = true
            )

            challenge_detail_title_edit.hint = title
            challenge_detail_description_edit.hint = description

            if (description.isBlank()) {
                challenge_detail_description_edit.hint = "Beschreibung"
            }

            setVisibility(challenge_detail_spinner_difficulties_edit, visible = true)
            when (difficulty) {
                Difficulty.LEICHT.toString() -> challenge_detail_spinner_difficulties_edit.setSelection(0)
                Difficulty.MITTEL.toString() -> challenge_detail_spinner_difficulties_edit.setSelection(1)
                Difficulty.SCHWER.toString() -> challenge_detail_spinner_difficulties_edit.setSelection(2)
            }
            setVisibility(challenge_detail_button_submit_edit, visible = true)

        }

        challenge_detail_stop_editing.setOnClickListener {
            setVisibility(
                challenge_detail_title,
                challenge_detail_description,
                challenge_detail_difficulty,
                challenge_detail_start_editing,
                switch_layout,
                visible = true
            )

            setVisibility(
                challenge_detail_header_edit,
                challenge_detail_stop_editing,
                layout_challenge_detail_title_edit,
                layout_challenge_detail_description_edit,
                challenge_detail_spinner_difficulties_edit,
                challenge_detail_button_submit_edit,
                visible = false
            )

            if (description.isBlank()) {
                setVisibility(challenge_detail_description, visible = false)
            }
        }

        challenge_detail_button_submit_edit.setOnClickListener {
            saveUpdatedChallenge()
        }

    }

    private fun setupInitialUI() {
        challenge_detail_title.text = title
        if (description.isBlank()) {
            challenge_detail_description.text = "Keine Beschreibung"
        } else {
            challenge_detail_description.text = description
        }

        challenge_detail_difficulty.text = "Schwierigkeit: $difficulty"

        val adapterDifficulties = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.difficulties_challenges,
            android.R.layout.simple_spinner_dropdown_item
        )
        challenge_detail_spinner_difficulties_edit.adapter = adapterDifficulties


        // get the saved switch state and set it
        sharedPrefs =
            activity?.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE) ?: return
        val switchState = sharedPrefs.getBoolean(Constants.PREFS_SWITCH_STATE + id, false)
        publish_switch.isChecked = switchState
        setStatus(switchState)

        publish_switch.setOnCheckedChangeListener { _, isChecked ->
            handleSwitchPositionChanged(isChecked)
        }
    }

    private fun handleSwitchPositionChanged(isChecked: Boolean) {
        val firstTimeChallengePublished =
            sharedPrefs.getBoolean(Constants.PREFS_FIRST_TIME_CHALLENGE_PUBLISHED, true)

        if (firstTimeChallengePublished) {
            sharedPrefs.edit().putBoolean(Constants.PREFS_FIRST_TIME_CHALLENGE_PUBLISHED, false).apply()

            //TODO: nur einmal anzeigen?? oder vielleicht besser jedesmal wenn er versucht sie zu bearbeiten!
            AlertDialog.Builder(requireContext())
                .setTitle("Hinweis")
                .setMessage("Eine veröffentlichte Challenge kann nicht bearbeitet werden. Wenn du die Challenge bearbeiten willst, musst du sie erst wieder auf \"Privat\" setzen")
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .show()
        }

        //update the public status of the challenge
        if (isChecked) {
            setVisibility(challenge_detail_start_editing, visible = false)
        } else {
            if (type == ChallengeType.USER_CHALLENGE && isEditable) {
                setVisibility(challenge_detail_start_editing, visible = true)
            }
        }

        overviewViewModel.updatePublicStatus(id, isChecked)
        sharedPrefs.edit().putBoolean(Constants.PREFS_SWITCH_STATE + id, isChecked).apply()

//          TODO: Hier wird noch primitiv eine Progressbar für 2s eingeblendet und dann der jeweils andere state angezeigt, obwohl nicht geschaut wird ob success oder failure

        setVisibility(challenge_detail_progressbar, visible = true)
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        /*
        runBlocking {
            switchStatus()
            delay(2000)
        }*/
        Handler().postDelayed(this::switchStatus, 2000)
    }

    //FIXME: lieber den Nutzer die felder bearbeiten lassen, die er auch bearbeiten will, statt alles wieder leer zu setzen!!
    // und auf keinen Fall verlangen dass der Titel neu eingegeben werden muss -> miese UX
    // stattdessen überprüfen, was sich verändert hat (vergleich if alterTitel != neuer Titel) und nur das updaten
    private fun saveUpdatedChallenge() {
        val newTitle = challenge_detail_title_edit.text.toString()
        /*
        if (newTitle.isBlank()) {
            layout_challenge_detail_title_edit.error = "Name erforderlich"
            return@setOnClickListener
        }*/
        val newDescription = challenge_detail_description_edit.text.toString()
        val difficulties = resources.getStringArray(R.array.difficulties_challenges)
        val newDifficulty: Difficulty = when (challenge_detail_spinner_difficulties_edit.selectedItem) {
            difficulties[0] -> Difficulty.LEICHT
            difficulties[1] -> Difficulty.MITTEL
            difficulties[2] -> Difficulty.SCHWER
            else -> Difficulty.LEICHT
        }

        val userid = sharedPrefs.getString(Constants.PREFS_USER_ID, "") ?: ""

        val newUserChallenge = UserChallenge(
            challengeId = id,
            title = newTitle,
            description = newDescription,
            difficulty = newDifficulty,
            type = ChallengeType.USER_CHALLENGE,
            completed = completed,
            isPublic = false,
            creatorId = userid
        )
        Timber.d("Geänderte Challenge: " + newUserChallenge.toString())

        publish_switch.isChecked = false

        overviewViewModel.updateChallenge(newUserChallenge)

        requireActivity().findNavController(R.id.nav_host_fragment).popBackStack()
    }

    /**
     * Util-Method to change visibility of the given views based on the given parameter "visible" (GONE if false, VISIBLE if true)
     */
    private fun setVisibility(vararg views: View, visible: Boolean) {
        val visibilityStatus = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }

        for (v in views) {
            v.visibility = visibilityStatus
        }
    }

    private fun setStatus(switchState: Boolean) {
        if (switchState) {
            setVisibility(challenge_detail_relativelayout_online, visible = true)
            setVisibility(challenge_detail_relativelayout_offline, challenge_detail_start_editing, visible = false)
        } else {
            setVisibility(challenge_detail_relativelayout_offline, challenge_detail_start_editing, visible = true)
            setVisibility(challenge_detail_relativelayout_online, visible = false)
        }
    }

    private fun switchStatus() {
        //TODO: app crasht hier, da challenge_detail_relativelayout_offline gleich null wenn geedited und dann neu geöffnet
        // -> sollte jetzt aber nicht mehr passieren können

        // bessere if bedingung hier nötig (ohne layout zu referenzieren, das vielleicht nicht sichtbar ist)!

        if (challenge_detail_relativelayout_offline.visibility == View.GONE) {
            setVisibility(challenge_detail_relativelayout_online, visible = false)
            setVisibility(challenge_detail_relativelayout_offline, visible = true)
        } else {
            setVisibility(challenge_detail_relativelayout_online, visible = true)
            setVisibility(challenge_detail_relativelayout_offline, visible = false)
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
