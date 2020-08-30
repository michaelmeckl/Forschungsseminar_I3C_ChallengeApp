package com.example.challengecovid.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.challengecovid.R
import com.example.challengecovid.adapter.AboutAdapter
import kotlinx.android.synthetic.main.fragment_about.*

// TODO: merge this with the about fragment and instead add a fragment for some corona guidelines and links to who etc.!

// TODO: außerdem die guidelines von der frau böhm mit für die challenges verwenden!!!!
class AboutFragment : Fragment() {

    private lateinit var aboutAdapter: AboutAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)     // this fragment wants to edit the toolbar menu

        aboutAdapter = AboutAdapter()
        aboutAdapter.attributionList = allAttributions

        about_recycler_view.apply {
            setHasFixedSize(true)
            adapter = aboutAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()    // don't show the menu in this fragment!
    }

    companion object {
        // TODO alle verwendeten Icons und andere Quellen hier auflisten!
        val allAttributions = listOf(
            AttributionItem(R.drawable.ic_coronavirus, "Icon made by Freepik from https://flaticon.com"),
            AttributionItem(R.drawable.icon_protective_suit_corona, "Illustration by Olha Khomich from https://icons8.com"),
            AttributionItem(R.drawable.ic_flame, "Taken from Streaky (https://github.com/alexakasanjeev/streak-counter)"),
            AttributionItem(R.drawable.ic_user_man_1,"Icon made by Freepik from www.flaticon.com/"),
            AttributionItem(R.drawable.ic_user_man_2,"Icon made by Freepik from www.flaticon.com/"),
            AttributionItem(R.drawable.ic_user_man_3,"Icon made by Freepik from www.flaticon.com/"),
            AttributionItem(R.drawable.ic_user_woman_1,"Icon made by Freepik from www.flaticon.com/"),
            AttributionItem(R.drawable.ic_user_woman_2,"Icon made by Freepik from www.flaticon.com/"),
            AttributionItem(R.drawable.ic_user_woman_3,"Icon made by Freepik from www.flaticon.com/"),
            AttributionItem(R.drawable.ic_healthy_category,"Icon made by Icongeek26 from www.flaticon.com/"),
            AttributionItem(R.drawable.ic_sport_category,"Icon made by Freepik from www.flaticon.com/"),
            AttributionItem(R.drawable.ic_relax_category,"Icon made by Freepik from www.flaticon.com/"),
            AttributionItem(R.drawable.ic_chore_category,"Icon made by Vectors Market from www.flaticon.com/"),
            AttributionItem(R.drawable.ic_pleasure_category,"Icon made by Freepik from www.flaticon.com/"),
            AttributionItem(R.drawable.ic_productivity_category,"Icon made by Freepik from www.flaticon.com/"),
            AttributionItem(R.drawable.ic_social_category,"Icon made by Freepik from www.flaticon.com/"),
            AttributionItem(R.drawable.ic_safety_category,"Icon made by Freepik from www.flaticon.com/")

        )
    }
}

// simple data class for the recycler items
data class AttributionItem(val imageResource: Int, val attribution: String)