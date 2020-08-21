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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.challengecovid.R
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.viewmodels.ChallengeViewModel2
import com.example.challengecovid.viewmodels.ChallengesViewModel
import kotlinx.android.synthetic.main.fragment_create_new_challenge.*
import timber.log.Timber
import java.util.*
import kotlin.random.Random
import kotlin.reflect.typeOf


class CreateChallengeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var root: View? = null
    private val challengesViewModel: ChallengesViewModel by viewModels()
    private lateinit var challengeViewModel2: ChallengeViewModel2
    private lateinit var spinnerDifficulties: Spinner
    private lateinit var spinnerIcons: Spinner
    private var selectedSpinnerDifficulty = "Einfach"
    private var selectedSpinnerIcon = "Kein Icon"


    fun CreateChallengeFragment() {

    }

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
        // Another interface callback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        challengeViewModel2 = ViewModelProvider(this).get(ChallengeViewModel2::class.java)

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
            val all_icons = resources.getStringArray(R.array.icons_challenges)
            when (selectedSpinnerIcon) {
                all_icons[0] -> challengeIcon = resources.getResourceEntryName(R.drawable.ic_trophy)
            }
            var newchallengeDuration: Float
            val selectedChallengeDuration = duration_create_new_challenge.text.toString()
            if (selectedChallengeDuration.isEmpty()) {
                newchallengeDuration = Float.POSITIVE_INFINITY
            } else {
                newchallengeDuration = selectedChallengeDuration.toFloat()
            }

            val newChallenge = Challenge(
                randomNum,
                name_create_new_challenge.text.toString(),
                description_create_new_challenge.text.toString(),
//                TODO: das ersetzen später
//                challengeIcon,
                R.drawable.ic_trophy,
                challengeXP,
                selectedSpinnerDifficulty,
                newchallengeDuration,
                Date().time
            )


            Timber.d(newChallenge.toString())
            /*
            val job = CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    dataSource.insert(dummyChallenge)
                }
            }
            */
            challengeViewModel2.insert(newChallenge)

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
