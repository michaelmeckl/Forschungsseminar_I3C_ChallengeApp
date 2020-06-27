package com.example.coronasimulation.ui.challenges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.coronasimulation.R
import kotlinx.android.synthetic.main.fragment_challenges.*

class ChallengesFragment : Fragment() {

    private val challengesViewModel: ChallengesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_challenges, container, false)

        challengesViewModel.text.observe(viewLifecycleOwner, Observer {
            text_challenges.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_first.setOnClickListener {
            println("Hello Button")
        }
    }
}