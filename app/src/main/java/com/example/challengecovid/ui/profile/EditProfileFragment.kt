package com.example.challengecovid.ui.profile

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.challengecovid.R
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.database.repository.UserRepository
import com.example.challengecovid.model.User
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.popup_edit_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class EditProfileFragment : Fragment() {

    //var profile = CharacterSelectActivity()
    //private var currentUserId = profile.currentUser.userId
    private lateinit var currentUser: User

    private lateinit var profileViewModel: ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.popup_edit_profile, container, false)
        val application: Application = requireNotNull(this.activity).application
        val db = ChallengeAppDatabase.getInstance(application, CoroutineScope(Dispatchers.IO))
        val userRepository = UserRepository(db)

        profileViewModel = getViewModel { ProfileViewModel(userRepository) }



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUser = profileViewModel.getUser("test test test") ?: return  //TODO richtige id


        val resID = resources.getIdentifier(currentUser.userIcon, "drawable", "com.example.challengecovid")
        profile_picture_edit_profile_fragment.setImageResource(resID)

        edit_profile_picture.setOnClickListener {
            requireActivity().findNavController(R.id.nav_host_fragment)
                .navigate(EditProfileFragmentDirections.actionNavigationEditProfileToNavigationEditProfilePicture())

        }

        save_changes.setOnClickListener {
            //remove this fragment from the backstack to navigate back
            val newName = change_name.toString()
            currentUser.username = newName
            profileViewModel.updateUser(currentUser)



            requireActivity().findNavController(R.id.nav_host_fragment).popBackStack()
        }
    }


}

/*
    companion object {

        private val IMAGE_PICK_CODE = 1000

        private val PERMISSION_CODE = 10001

    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out kotlin.String>, grantResults: IntArray): Unit {
       when (requestCode){
           PERMISSION_CODE -> {
               if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                   pickImageFromGallery()
               }else {
                   // Explain to the user that the feature is unavailable because
                   // the features requires a permission that the user has denied.
                   // At the same time, respect the user's decision. Don't link to
                   // system settings in an effort to convince the user to change
                   // their decision.
               }
               return
           }
       }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            profile_picture_edit.setImageURI(data?.data)
            //bild an datenbank schicken


        }
    }
*/

 */



