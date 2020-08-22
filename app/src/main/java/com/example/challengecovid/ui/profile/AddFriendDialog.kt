package com.example.dialogfragment_example

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.challengecovid.R
import kotlinx.android.synthetic.main.popup_add_friend.*
import kotlinx.android.synthetic.main.popup_edit_profile.view.*
import androidx.navigation.findNavController



class AddFriendDialog : DialogFragment() {

    companion object {

        const val TAG = "AddFriendDialog"

    }



    private lateinit var viewModel: PopupViewModel

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

        friend_invitation.setOnClickListener{
            var txt = new_friend_name.toString()
            dismiss()
        }


    }





}