package com.example.coronasimulation.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.coronasimulation.R
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment() {

    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_news, container, false)

        newsViewModel.text.observe(viewLifecycleOwner, Observer {
            text_news.text = it
        })
        return root
    }
}