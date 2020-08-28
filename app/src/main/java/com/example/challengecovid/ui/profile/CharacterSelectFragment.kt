package com.example.challengecovid.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.model.User
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.fragment_character_selection.*

class CharacterSelectFragment : Fragment(), View.OnClickListener {

    private lateinit var chosenPicture: String
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_character_selection, container, false)

        val userRepository = RepositoryController.getUserRepository()
        val application = requireNotNull(this.activity).application
        profileViewModel = getViewModel { ProfileViewModel(userRepository, application) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firstRun = TODO()

        set_profile_picture_1.setOnClickListener(this)
        set_profile_picture_2.setOnClickListener(this)
        set_profile_picture_3.setOnClickListener(this)
        set_profile_picture_4.setOnClickListener(this)
        set_profile_picture_5.setOnClickListener(this)
        set_profile_picture_6.setOnClickListener(this)

        save_profile.setOnClickListener {
            val name = set_name.toString()

            if (isNullOrEmpty(name)) {
                showEnterNameMessage()
                return@setOnClickListener
            }

            if (firstRun) {
                insertUser(name)
                requireActivity().findNavController(R.id.nav_host_fragment).popBackStack()      //TODO: does this work?
            } else {
                updateUser(name)
                requireActivity().findNavController(R.id.nav_host_fragment).popBackStack()
            }
        }
    }

    private fun showEnterNameMessage() {
        Toast.makeText(requireActivity(), "Gib einen Namen ein", Toast.LENGTH_SHORT).show()
    }

    private fun insertUser(name: String) {
        val newUser = User(username = name, userIcon = chosenPicture)
        profileViewModel.insertNewUser(newUser)
    }

    private fun updateUser(name: String) {
        val currentUser = profileViewModel.currentUser.value

        if (currentUser != null) {
            currentUser.username = name
            currentUser.userIcon = chosenPicture

            profileViewModel.updateUser(currentUser)
        }
    }

    private fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && str.isNotEmpty())
            return false
        return true
    }

    override fun onClick(v: View?) {
        v ?: return

        when (v.id) {
            R.id.set_profile_picture_1 -> chosenPicture = "ic_user_man_1"
            R.id.set_profile_picture_2 -> chosenPicture = "ic_user_man_2"
            R.id.set_profile_picture_3 -> chosenPicture = "ic_user_woman_1"
            R.id.set_profile_picture_4 -> chosenPicture = "ic_user_woman_2"
            R.id.set_profile_picture_5 -> chosenPicture = "ic_user_woman_3"
            R.id.set_profile_picture_6 -> chosenPicture = "ic_user_man_3"
        }

        val resID = resources.getIdentifier(chosenPicture, "drawable", activity?.packageName)
        set_profile_picture.setImageResource(resID)

    }
}

