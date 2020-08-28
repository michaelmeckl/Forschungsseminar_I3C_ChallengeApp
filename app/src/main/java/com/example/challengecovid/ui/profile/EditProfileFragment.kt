package com.example.challengecovid.ui.profile

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.model.User
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.popup_edit_profile.*


class EditProfileFragment : Fragment() {

    private lateinit var currentUser: User
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_character_selection, container, false)

        val application: Application = requireNotNull(this.activity).application
        val userRepository = RepositoryController.getUserRepository()
        profileViewModel = getViewModel { ProfileViewModel(userRepository, application) }

        return root
    }

    //TODO: this is just duplicate code basically
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUser = profileViewModel.currentUser.value ?: return

        val resID = resources.getIdentifier(currentUser.userIcon, "drawable", "com.example.challengecovid")
        profile_picture_edit_profile_fragment.setImageResource(resID)

        save_changes.setOnClickListener {
            //remove this fragment from the backstack to navigate back
            val newName = change_name.toString()
            currentUser.username = newName
            profileViewModel.updateUser(currentUser)

            requireActivity().findNavController(R.id.nav_host_fragment).popBackStack()
        }
    }
}


