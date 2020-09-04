package com.example.challengecovid.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.BoringLayout
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


//FIXME: wenn eine challenge von anderen aus dem feed angenommen wird, kann man sie zwar nicht bearbeiten oder veröffentlichen, das liegt aber daran dass sie automatisch als completed markiert ist sobald angenommen ????
// -> aber nicht immer? manchmal gehts auch? ich bin verwirrt ...
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

        //FIXME: statt alles ständig auf hidden oder visible zu setzen wäre hier ein neues eigenes Layout deutlich besser
        if (type == ChallengeType.SYSTEM_CHALLENGE || !isEditable) {
            // hide the option to publish for system challenges
            publish_switch.visibility = View.GONE

            challenge_detail_start_editing.visibility = View.GONE
            challenge_detail_relativelayout_online.visibility = View.GONE
            challenge_detail_relativelayout_offline.visibility = View.GONE
        }

        challenge_detail_start_editing.setOnClickListener {
            challenge_detail_title.visibility = View.GONE
            challenge_detail_description.visibility = View.GONE
            challenge_detail_difficulty.visibility = View.GONE
            challenge_detail_start_editing.visibility = View.GONE

            challenge_detail_header_edit.visibility = View.VISIBLE
            challenge_detail_stop_editing.visibility = View.VISIBLE
            layout_challenge_detail_title_edit.visibility = View.VISIBLE
            challenge_detail_title_edit.hint = title
            layout_challenge_detail_description_edit.visibility = View.VISIBLE
            challenge_detail_description_edit.hint = description

            if (description.isBlank()) {
                challenge_detail_description_edit.hint = "Beschreibung"
            }

            challenge_detail_spinner_difficulties_edit.visibility = View.VISIBLE
            when (difficulty) {
                Difficulty.LEICHT.toString() -> challenge_detail_spinner_difficulties_edit.setSelection(0)
                Difficulty.MITTEL.toString() -> challenge_detail_spinner_difficulties_edit.setSelection(1)
                Difficulty.SCHWER.toString() -> challenge_detail_spinner_difficulties_edit.setSelection(2)
            }
            challenge_detail_button_submit_edit.visibility = View.VISIBLE

        }

        challenge_detail_stop_editing.setOnClickListener {
            challenge_detail_title.visibility = View.VISIBLE
            challenge_detail_description.visibility = View.VISIBLE
            challenge_detail_difficulty.visibility = View.VISIBLE
            challenge_detail_start_editing.visibility = View.VISIBLE

            challenge_detail_header_edit.visibility = View.GONE
            challenge_detail_stop_editing.visibility = View.GONE
            layout_challenge_detail_title_edit.visibility = View.GONE
            layout_challenge_detail_description_edit.visibility = View.GONE
            challenge_detail_spinner_difficulties_edit.visibility = View.GONE
            challenge_detail_button_submit_edit.visibility = View.GONE
            if (description.isBlank()) {
                challenge_detail_description.visibility = View.GONE
            }
        }

        challenge_detail_button_submit_edit.setOnClickListener {

            //FIXME: lieber den Nutzer die felder bearbeiten lassen, die er auch bearbeiten will, statt alles wieder leer zu setzen!!
            // und auf keinen Fall verlangen dass der Titel neu eingegeben werden muss -> miese UX
            // stattdessen überprüfen, was sich verändert hat (vergleich if alterTitel != neuer Titel) und nur das updaten
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
        sharedPrefs = activity?.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE) ?: return
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

            //TODO: nur einmal anzeigen?? vielleicht besser jedesmal wenn er versucht sie zu bearbeiten!
            AlertDialog.Builder(requireContext())
                .setTitle("Hinweis")
                .setMessage("Eine veröffentlichte Challenge kann nicht bearbeitet werden. Wenn du die Challenge bearbeiten willst, musst du sie erst wieder auf 'nicht veröffentlicht' setzen")
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .show()
        }

        //update the public status of the challenge
        if (isChecked) {
            challenge_detail_start_editing.visibility = View.GONE
            overviewViewModel.updatePublicStatus(id, isChecked)
        } else {
            if (type == ChallengeType.USER_CHALLENGE && isEditable) {
                challenge_detail_start_editing.visibility = View.VISIBLE
            }
        }

        sharedPrefs.edit().putBoolean(Constants.PREFS_SWITCH_STATE + id, isChecked).apply()

//          TODO: Hier wird noch primitiv eine Progressbar für 2s eingeblendet und dann der jeweils andere state angezeigt, obwohl nicht geschaut wird ob success oder failure

        challenge_detail_progressbar.visibility = View.VISIBLE
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

    //FIXME: man kann das veröffentlichen layout noch sehen wenn im bearbeitungsmodus! Das führt ständig zu abstürzen!!!

    private fun setStatus(switchState: Boolean) {
        if (switchState) {
            challenge_detail_relativelayout_offline.visibility = View.GONE
            challenge_detail_relativelayout_online.visibility = View.VISIBLE
            challenge_detail_start_editing.visibility = View.GONE
        } else {
            challenge_detail_relativelayout_online.visibility = View.GONE
            challenge_detail_relativelayout_offline.visibility = View.VISIBLE
            challenge_detail_start_editing.visibility = View.VISIBLE
        }
    }

    private fun switchStatus() {
        //TODO: app crasht hier!!!!!!
        // challenge_detail_relativelayout_offline ist null wenn geedited und dann neu geöffnet
        //challenge_detail_relativelayout_offline ?: return
        if (challenge_detail_relativelayout_offline.visibility == View.GONE) {
            challenge_detail_relativelayout_online.visibility = View.GONE
            challenge_detail_relativelayout_offline.visibility = View.VISIBLE
        } else {
            challenge_detail_relativelayout_offline.visibility = View.GONE
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
