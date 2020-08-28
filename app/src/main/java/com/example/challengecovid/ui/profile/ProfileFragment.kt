package com.example.challengecovid.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.challengecovid.Constants
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var myDialog: Dialog
    private lateinit var profileViewModel: ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val userRepository = RepositoryController.getUserRepository()
        profileViewModel = getViewModel { ProfileViewModel(userRepository) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPrefs =
            activity?.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
        val currentUserId = sharedPrefs?.getString(Constants.PREFS_USER_ID, "") ?: ""

        // return early if fetching the user id didn't work
        if (currentUserId == "") return

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