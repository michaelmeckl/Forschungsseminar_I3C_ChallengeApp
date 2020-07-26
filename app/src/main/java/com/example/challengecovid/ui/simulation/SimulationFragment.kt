package com.example.challengecovid.ui.simulation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.challengecovid.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_simulation.*

class SimulationFragment : Fragment() {

    private val simulationViewModel: SimulationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_simulation, container, false)

        simulationViewModel.text.observe(viewLifecycleOwner, Observer {
            text_simulation.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab_button.setOnClickListener { button ->
            Snackbar.make(button, "This is a snackbar message!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}