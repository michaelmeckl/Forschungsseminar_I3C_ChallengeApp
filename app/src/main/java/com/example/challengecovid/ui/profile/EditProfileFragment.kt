package com.example.challengecovid.ui.profile

import android.os.Build
import android.os.Bundle

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.challengecovid.R
import kotlinx.android.synthetic.main.popup_edit_profile.*
import kotlinx.android.synthetic.main.popup_edit_profile.view.*

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager

import android.os.Build.*
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.navigation.findNavController
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.database.repository.UserRepository
import com.example.challengecovid.ui.MainActivity
import com.example.challengecovid.ui.profile.ProfileFragment
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class EditProfileFragment : Fragment() {

    lateinit var myDialog: Dialog
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

        edit_profile_picture.setOnClickListener {
            requireActivity().findNavController(R.id.nav_host_fragment)
                .navigate(EditProfileFragmentDirections.actionNavigationEditProfileToNavigationEditProfilePicture())

        }

        save_changes.setOnClickListener {
            //remove this fragment from the backstack to navigate back
            val newName = change_name.toString()
            val someId = "123456"
            profileViewModel.updateUserName(someId,newName)
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



