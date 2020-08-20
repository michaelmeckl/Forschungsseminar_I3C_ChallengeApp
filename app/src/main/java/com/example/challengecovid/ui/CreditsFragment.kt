package com.example.challengecovid.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.challengecovid.R
import com.example.challengecovid.adapter.CreditsAdapter
import kotlinx.android.synthetic.main.fragment_credits.*

class CreditsFragment : Fragment() {

    private lateinit var creditsAdapter: CreditsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_credits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)     // this fragment wants to edit the toolbar menu

        creditsAdapter = CreditsAdapter()
        creditsAdapter.creditsList = allCredits

        credits_recycler_view.apply {
            setHasFixedSize(true)
            adapter = creditsAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()    // don't show the menu in this fragment!
    }

    companion object {
        // TODO alle verwendeten Icons und andere Quellen hier auflisten!
        // all attributions go here:
        val allCredits = listOf(
            CreditsItem(R.drawable.ic_coronavirus, "Icon made by Freepik from https://flaticon.com"),
            CreditsItem(R.drawable.icons8_user_male, "Icon made by Freepik from https://flaticon.com")
        )
    }
}

// simple data class for the recycler items
data class CreditsItem(val imageResource: Int, val attribution: String)