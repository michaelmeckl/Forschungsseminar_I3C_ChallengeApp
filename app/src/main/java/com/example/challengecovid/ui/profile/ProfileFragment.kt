package com.example.challengecovid.ui.profile

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.challengecovid.R
import com.example.challengecovid.adapter.RecyclerAdapter
import kotlinx.android.synthetic.main.fragment_profile.*
import androidx.navigation.findNavController
import com.example.challengecovid.viewmodels.ProfileViewModel


class ProfileFragment : Fragment() {

    private lateinit var myDialog: Dialog
    private lateinit var recyclerAdapter: RecyclerAdapter
    private val profileViewModel: ProfileViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        add_friend_button.setOnClickListener {
            requireActivity().findNavController(R.id.nav_host_fragment)
                .navigate(ProfileFragmentDirections.actionProfileFragmentToAddFriend())
        }

        edit_profile.setOnClickListener {
            requireActivity().findNavController(R.id.nav_host_fragment)
                .navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileDialog())
        }
    }


}