package com.example.challengecovid.ui.profile

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.challengecovid.R
import kotlinx.android.synthetic.main.fragment_profile.*
import androidx.navigation.findNavController
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.database.repository.UserRepository
import com.example.challengecovid.model.User
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    //var profile = CharacterSelectActivity()     //TODO: NEVER DO SOMETHING LIKE THIS!!! sowas muss später initialisiert werden, vgl. Fragment Lifecycle mal wieder
    private var currentUserId: String? = null
    private lateinit var currentUser: User


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val application: Application = requireNotNull(this.activity).application
        val db = ChallengeAppDatabase.getInstance(application, CoroutineScope(Dispatchers.IO))
        val userRepository = UserRepository(db)

        profileViewModel = getViewModel { ProfileViewModel(userRepository) }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //currentUserId = (profile as CharacterSelectActivity).currentUser.userId
        /*
        currentUser = profileViewModel.getUser("test test test") ?: return  //TODO

        val resID = resources.getIdentifier(currentUser.userIcon, "drawable", "com.example.challengecovid")
        profile_picture.setImageResource(resID)

        val name = currentUser.username
        profile_name.text = name
        */
        add_friend_button.setOnClickListener {
            requireActivity().findNavController(R.id.nav_host_fragment)
                .navigate(ProfileFragmentDirections.actionProfileFragmentToAddFriend())
        }

        edit_profile.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileToCharacterSelect("editProfile")
            requireActivity().findNavController(R.id.nav_host_fragment)
                .navigate(action)
        }


    }


}