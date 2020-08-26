package com.example.challengecovid.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.challengecovid.R
import kotlinx.android.synthetic.main.popup_add_friend.*
import com.example.challengecovid.viewmodels.ProfileViewModel


class AddFriendFragment : DialogFragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.popup_add_friend, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friend_invitation.setOnClickListener {
            var txt = new_friend_name.toString()
            dismiss()
        }


    }

}