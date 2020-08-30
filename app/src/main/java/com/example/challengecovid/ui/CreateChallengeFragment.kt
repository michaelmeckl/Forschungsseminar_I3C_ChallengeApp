package com.example.challengecovid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

//        provideMoreInformation()

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

//    private fun provideMoreInformation() {
//        // Toast showing more info for duration edittext
//        info_duration_create_challenge.setOnClickListener {
//            Toast.makeText(
//                requireContext(),
//                "Gib an, wie viele Tage lang du deine Challenge machen willst. Lass das Feld leer, wenn die Challenge unendlich lang drin bleiben soll",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//
//    }

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


    }

    //TODO: das funktioniert iwie nicht immer ?? (z.B. nach rotation wird nichts angezeigt manchmal?)
    private fun addNewChallenge() {
        val difficulties = resources.getStringArray(R.array.difficulties_challenges)

        layout_name_create_new_challenge.error = ""

        val challengeDifficulty: Difficulty = when (selectedSpinnerDifficulty) {
            difficulties[0] -> Difficulty.LEICHT
            difficulties[1] -> Difficulty.MITTEL
            difficulties[2] -> Difficulty.SCHWER
            else -> Difficulty.LEICHT
        }

        val sharedPrefs =
            activity?.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        val currentUserId = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""

        // return early if fetching the user id didn't work
        if (currentUserId == "") return

        val newChallenge = UserChallenge(
            title = name_create_new_challenge.text.toString(),
            description = description_create_new_challenge.text.toString(),
            difficulty = challengeDifficulty,
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
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        TODO("not implemented")
    }
}
