package com.example.dialogfragment_example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.challengecovid.R
import kotlinx.android.synthetic.main.popup_edit_profile.view.*

//imoplementation via https://blog.mindorks.com/implementing-dialog-fragment-in-android

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
        return inflater.inflate(R.layout.popup_add_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PopupViewModel::class.java)
        setupClickListeners(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupClickListeners(view: View) {
        view.save_changes.setOnClickListener {
            viewModel.sendName(view.change_name.text.toString())
            dismiss()
        }
    }

}