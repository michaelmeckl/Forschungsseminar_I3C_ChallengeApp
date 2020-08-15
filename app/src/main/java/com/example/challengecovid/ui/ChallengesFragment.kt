package com.example.challengecovid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.challengecovid.R
import com.example.challengecovid.viewmodels.ChallengesViewModel
import kotlinx.android.synthetic.main.fragment_challenges.*


class ChallengesFragment : Fragment() {

    private val challengesViewModel: ChallengesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_challenges, container, false)

//        challengesViewModel.text.observe(viewLifecycleOwner, Observer {
//            text_challenges.text = it
//        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        button_first.setOnClickListener {
//            println("Hello Button")
//        }

        fab_create_challenge.setOnClickListener {
            val newFragment: Fragment = CreateChallengeFragment()
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