package com.example.challengecovid.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.challengecovid.R
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.database.repository.ChallengeRepository
import com.example.challengecovid.model.Difficulty
import com.example.challengecovid.model.UserChallenge
import com.example.challengecovid.viewmodels.ChallengeListViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.fragment_create_new_challenge.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import kotlin.random.Random


class CreateChallengeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var challengeListViewModel: ChallengeListViewModel
    private lateinit var spinnerDifficulties: Spinner
    private lateinit var spinnerIcons: Spinner
    private var selectedSpinnerDifficulty = "Einfach"
    private var selectedSpinnerIcon = "Kein Icon"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_new_challenge, container, false)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        when(parent) {
            spinner_create_new_challenge -> selectedSpinnerDifficulty = parent.selectedItem.toString()
            spinner_icon_create_new_challenge -> selectedSpinnerIcon = parent.selectedItem.toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        TODO("not implemented")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application: Application = requireNotNull(this.activity).application
        val db = ChallengeAppDatabase.getInstance(
            application,
            CoroutineScope(Dispatchers.IO)
        )
        val challengeRepository = ChallengeRepository(db)
        challengeListViewModel = getViewModel { ChallengeListViewModel(challengeRepository) }

        // init adapter for difficulty spinner
//        var adapter1 = ArrayAdapter.createFromResource(
//            requireContext(),
//            R.array.difficulties_challenges,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spinner_create_new_challenge.adapter = adapter
//        }
        spinnerDifficulties = spinner_create_new_challenge
        val adapterDifficulties = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.difficulties_challenges,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinnerDifficulties.adapter = adapterDifficulties
        spinnerDifficulties.onItemSelectedListener = this


//         init adapter for icon spinner
//        ArrayAdapter.createFromResource(
//            requireContext(),
//            R.array.icons_challenges,
//            android.R.layout.simple_spinner_dropdown_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spinner_icon_create_new_challenge.adapter = adapter
//        }
        spinnerIcons = spinner_icon_create_new_challenge
        val adapterIcons = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.icons_challenges,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinnerIcons.adapter = adapterIcons
        spinnerIcons.onItemSelectedListener = this


        // Toast showing more info for duration edittext
        info_duration_create_challenge.setOnClickListener {
//            Snackbar.make(requireView(), "Gib an, wie viele Tage lang du deine Challenge machen willst. Lass das Feld leer, wenn die Challenge unendlich lang drin bleiben soll", Snackbar.LENGTH_LONG).show()
            Toast.makeText(
                requireContext(),
                "Gib an, wie viele Tage lang du deine Challenge machen willst. Lass das Feld leer, wenn die Challenge unendlich lang drin bleiben soll",
                Toast.LENGTH_LONG
            ).show()
        }

        
        button_submit_create_new_challenge.setOnClickListener {
            if (name_create_new_challenge.text.toString().isBlank()) {
                layout_name_create_new_challenge.error = "Braucht nen namen alter"
                return@setOnClickListener
            }
            //TODO: Kategorien/Icons hinzufügen
            layout_name_create_new_challenge.error = ""
            val randomNum = Random.nextInt()
            Timber.tag("randomNumber").d(randomNum.toString())

//            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
//                // An item was selected. You can retrieve the selected item using
//                // parent.getItemAtPosition(pos)
//            }
            var challengeXP = 0
            challengeXP = when (selectedSpinnerDifficulty){
                "Einfach" -> 5
                "Mittel" -> 10
                "Schwer" -> 20
                else -> 0
            }

            var challengeIcon = resources.getResourceEntryName(R.drawable.ic_trophy)
            val allIcons = resources.getStringArray(R.array.icons_challenges)
            when (selectedSpinnerIcon) {
                allIcons[0] -> challengeIcon = resources.getResourceEntryName(R.drawable.ic_trophy)
            }
            val newchallengeDuration: Float
            val selectedChallengeDuration = duration_create_new_challenge.text.toString()
            newchallengeDuration = if (selectedChallengeDuration.isEmpty()) {
                Float.POSITIVE_INFINITY
            } else {
                selectedChallengeDuration.toFloat()
            }

            val newChallenge = UserChallenge(
                name_create_new_challenge.text.toString(),
                description_create_new_challenge.text.toString(),
                Difficulty.MITTEL,
                false,
                "123456789"     //TODO: hier später die userId des nutzers rein, der diese challenge angelegt hat
            )


            Timber.d(newChallenge.toString())

            challengeListViewModel.insert(newChallenge)

            // Navigate back to ChallengesFragment
            val newFragment: Fragment = ChallengesFragment()
            val transaction = requireFragmentManager().beginTransaction()

            transaction.replace(
                R.id.nav_host_fragment,
                newFragment
            )

            transaction.addToBackStack(null) // if written, this transaction will be added to backstack

            transaction.commit()

        }
    }

}
