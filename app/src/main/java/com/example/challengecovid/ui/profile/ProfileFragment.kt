package com.example.challengecovid.ui.profile

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.challengecovid.R
import com.example.challengecovid.Utils
import com.example.challengecovid.adapter.RecyclerAdapter
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.ui.MainActivity
import com.example.challengecovid.ui.OverviewFragment
import com.example.challengecovid.viewmodels.OverviewViewModel
import com.example.dialogfragment_example.AddFriendDialog
import com.example.dialogfragment_example.EditProfileDialog
import kotlinx.android.synthetic.main.fragment_challenges.*
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.popup_edit_profile.*
import java.util.*
import com.example.dialogfragment_example.PopupViewModel as PopupViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import androidx.navigation.findNavController


class ProfileFragment : Fragment() {

    private lateinit var myDialog: Dialog
    private lateinit var recyclerAdapter: RecyclerAdapter
    private val popupViewModel: PopupViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

/*
popupViewModel = ViewModelProvider(this).get(PopupViewModel::class.java)
PopupViewModel.name.observe(this, Observer {
datenbank
})
*/

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        add_friend_button.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragment = AddFriendDialog()
            fragment.show(fragmentManager,"AddFriend")
        }

        edit_profile.setOnClickListener {
           view.findNavController().navigate(R.id.action_profileFragment_to_editProfileDialog)
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment = EditProfileDialog()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentTransaction.commit()


        }
    }






}