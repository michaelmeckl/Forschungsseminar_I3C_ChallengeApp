package com.example.challengecovid.ui.profile

import android.app.Application
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
import com.example.challengecovid.model.User
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.popup_edit_profile.*


class EditProfileFragment : Fragment() {

    private lateinit var currentUser: User
    private lateinit var profileViewModel: ProfileViewModel

    private val sharedPrefs =
        activity?.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
    private val currentUserId = sharedPrefs?.getString(Constants.PREFS_USER_ID, null)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.popup_edit_profile, container, false)

        val application: Application = requireNotNull(this.activity).application
        val userRepository = RepositoryController.getUserRepository()
        profileViewModel = getViewModel { ProfileViewModel(userRepository, application) }

        return root
    }

    //TODO: this is just duplicate code basically
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         val sharedPrefs = activity?.getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
         val currentUserId = sharedPrefs?.getString(Constants.PREFS_USER_ID, null)

        edit_profile_picture.setOnClickListener {
            val fragment = CharacterSelectFragment()
            fragment.show(requireActivity().supportFragmentManager, null)
        }
        save_changes.setOnClickListener {
            //remove this fragment from the backstack to navigate back
            val newName = change_name.toString()

            profileViewModel.updateUserName(newName)

            requireActivity().findNavController(R.id.nav_host_fragment).popBackStack()
        }
    }

    /*
    private fun setUpViews(){


        change_name.text = currentUser?.username.toString()
        val resID = resources.getIdentifier(currentUser?.userIcon, "drawable", activity?.packageName)
        profile_picture_edit_profile_fragment.setImageResource(resID)
    }
    
     */
}


