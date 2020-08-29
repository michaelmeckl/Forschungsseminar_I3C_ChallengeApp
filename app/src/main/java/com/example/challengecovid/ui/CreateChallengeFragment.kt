package com.example.challengecovid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import com.example.challengecovid.Constants
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.model.Difficulty
import com.example.challengecovid.model.UserChallenge
import com.example.challengecovid.viewmodels.OverviewViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.fragment_create_new_challenge.*
import timber.log.Timber


class CreateChallengeFragment : DialogFragment(), AdapterView.OnItemSelectedListener {

    private lateinit var overviewViewModel: OverviewViewModel

    private var selectedSpinnerDifficulty = "Einfach"
    private var selectedSpinnerIcon = "Kein Icon"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.dialogFullScreen)    // set a custom style to make the dialog fragment bigger
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_new_challenge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val challengeRepository = RepositoryController.getChallengeRepository()
        val userRepository = RepositoryController.getUserRepository()
        val application = requireNotNull(this.activity).application
        overviewViewModel = getViewModel { OverviewViewModel(challengeRepository, userRepository, application) }

        setupSpinner()

        provideMoreInformation()

        button_submit_create_new_challenge.setOnClickListener {
            // check if a name was provided
            if (name_create_new_challenge.text.toString().isBlank()) {
                layout_name_create_new_challenge.error = "Braucht nen namen alter"      //TODO ... ¯\_(ツ)_/¯
                return@setOnClickListener
            }

            // create and add the challenge to the database
            addNewChallenge()

            // Navigate back to ChallengesFragment
            navigateBack()
        }
    }

    private fun provideMoreInformation() {
        // Toast showing more info for duration edittext
        info_duration_create_challenge.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Gib an, wie viele Tage lang diese Challenge aktiv sein soll. Wenn nichts eingegeben wird werden 7 Tage angenommen.",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun setupSpinner() {
        // init adapter for difficulty spinner
        /*
        var adapter1 = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.difficulties_challenges,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner_create_new_challenge.adapter = adapter
        }
        */

        val adapterDifficulties = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.difficulties_challenges,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner_difficulties.adapter = adapterDifficulties
        spinner_difficulties.onItemSelectedListener = this


        /*
        //init adapter for icon spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.icons_challenges,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner_icon_create_new_challenge.adapter = adapter
        }
        spinnerIcons = spinner_icon_create_new_challenge
        val adapterIcons = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.icons_challenges,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinnerIcons.adapter = adapterIcons
        spinnerIcons.onItemSelectedListener = this
         */
    }

    private fun addNewChallenge() {
        layout_name_create_new_challenge.error = ""

        //TODO: use the difficulty property of the challenge instead
        var challengeXP = 0
        challengeXP = when (selectedSpinnerDifficulty) {
            "Einfach" -> 5
            "Mittel" -> 10
            "Schwer" -> 20
            else -> 0
        }

        //TODO: vorerst vllt keine Icons für UserChallenges?
        var challengeIcon = resources.getResourceEntryName(R.drawable.ic_trophy)
        val allIcons = resources.getStringArray(R.array.icons_challenges)
        when (selectedSpinnerIcon) {
            allIcons[0] -> challengeIcon = resources.getResourceEntryName(R.drawable.ic_trophy)
        }

        val newchallengeDuration: Float
        val selectedChallengeDuration = duration_create_new_challenge.text.toString()
        newchallengeDuration = if (selectedChallengeDuration.isEmpty()) {
            Float.POSITIVE_INFINITY     //TODO: besserer default als unendlich, vllt sowas wie 7 (Tage)
        } else {
            selectedChallengeDuration.toFloat()
        }

        val sharedPrefs =
            activity?.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        val currentUserId = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""

        // return early if fetching the user id didn't work
        if (currentUserId == "") return

        val newChallenge = UserChallenge(
            title = name_create_new_challenge.text.toString(),
            description = description_create_new_challenge.text.toString(),
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 2,
            creatorId = currentUserId
        )

        Timber.d(newChallenge.toString())
        overviewViewModel.addNewChallenge(newChallenge)
    }


    private fun navigateBack() {
        //activity?.onBackPressed()
        requireActivity().findNavController(R.id.nav_host_fragment).popBackStack()
    }


    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        when (parent) {
            spinner_difficulties -> selectedSpinnerDifficulty = parent.selectedItem.toString()
            //spinner_icon_create_new_challenge -> selectedSpinnerIcon = parent.selectedItem.toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        TODO("not implemented")
    }
}
