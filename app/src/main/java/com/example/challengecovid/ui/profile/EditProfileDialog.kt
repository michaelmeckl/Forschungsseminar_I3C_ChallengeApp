package com.example.dialogfragment_example

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.challengecovid.R
import kotlinx.android.synthetic.main.popup_edit_profile.*
import kotlinx.android.synthetic.main.popup_edit_profile.view.*

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager

import android.os.Build.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.example.challengecovid.ui.MainActivity
import com.example.challengecovid.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*


class EditProfileDialog : Fragment() {

    private val permissionslist = arrayOf(READ_EXTERNAL_STORAGE)

    companion object {

        private val IMAGE_PICK_CODE = 1000

        private val PERMISSION_CODE = 10001

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popup_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edit_profile_picture.setOnClickListener{
            requestPermissions(permissionslist, IMAGE_PICK_CODE)
        }

        save_changes.setOnClickListener{
            //namensänderung in die datenbank schicken
            val fragmentManager = childFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment = ProfileFragment()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentTransaction.commit()
        }
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


}








