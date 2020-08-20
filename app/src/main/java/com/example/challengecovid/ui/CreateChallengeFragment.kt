package com.example.challengecovid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_create_new_challenge.*
import timber.log.Timber
import java.util.*
import kotlin.random.Random


class CreateChallengeFragment : Fragment() {

    private var root: View? = null
    private val challengesViewModel: ChallengesViewModel by viewModels()
    private lateinit var challengeViewModel2: ChallengeViewModel2

    fun CreateChallengeFragment() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_new_challenge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        challengeViewModel2 = ViewModelProvider(this).get(ChallengeViewModel2::class.java)

        // init adapter for difficulty spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.difficulties_challenges,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner_create_new_challenge.adapter = adapter
        }

        // init adapter for icon spinner
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

            val dummyChallenge = Challenge(
                randomNum,
                "New dummy challenge",
                "A new challenge was created! Good job!",
                R.drawable.ic_trophy,
                4,
                "easy",
                2f,
                Date().time
            )

            /*
            val job = CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    dataSource.insert(dummyChallenge)
                }
            }
            */
            challengeViewModel2.insert(dummyChallenge)

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
