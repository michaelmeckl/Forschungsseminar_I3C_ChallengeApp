package com.example.challengecovid.ui.profile

import android.app.Application
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import com.example.challengecovid.R
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.database.repository.UserRepository
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.popup_edit_profile_picture.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class EditProfilePictureDialogFragment: DialogFragment(), View.OnClickListener {

    private lateinit var chosenPicture: String

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.popup_edit_profile_picture, container, false)

        val application: Application = requireNotNull(this.activity).application
        val db = ChallengeAppDatabase.getInstance(application, CoroutineScope(Dispatchers.IO))
        val userRepository = UserRepository(db)
        profileViewModel = getViewModel { ProfileViewModel(userRepository) }

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile_picture_1.setOnClickListener(this)
        profile_picture_2.setOnClickListener(this)
        profile_picture_3.setOnClickListener(this)
        profile_picture_4.setOnClickListener(this)
        profile_picture_5.setOnClickListener(this)
        profile_picture_6.setOnClickListener(this)

        save_changes_profile_picture.setOnClickListener{
            val someID = "123456"
            profileViewModel.updateUserIcon(someID, chosenPicture)

            val bmImg = BitmapFactory.decodeFile(chosenPicture)
            profile_picture.setImageBitmap(bmImg)
            requireActivity().findNavController(R.id.nav_host_fragment).popBackStack()
        }

    }

    override fun onClick(v: View?){
            when(v!!.id){
                R.id.profile_picture_1 -> {
                    profile_picture_new.setImageResource(R.drawable.iconfinder_avatar_367_456319_6415373)
                    chosenPicture = "drawable/iconfinder_avatar_367_456319_6415373.png"

                }
                R.id.profile_picture_2 -> {
                    profile_picture_new.setImageResource(R.drawable.iconfinder_avatar_368_456320_6415359)
                    chosenPicture = "drawable/iconfinder_avatar_368_456320_6415359.png"
                }
                R.id.profile_picture_3 -> {
                    profile_picture_new.setImageResource(R.drawable.iconfinder_avatar_369_456321_6415374)
                    chosenPicture = "drawable/iconfinder_avatar_369_456321_6415374.png"
                }
                R.id.profile_picture_4 -> {
                    profile_picture_new.setImageResource(R.drawable.iconfinder_avatar_370_456322_6415362)
                    chosenPicture = "drawable/iconfinder_avatar_370_456322_6415362.png"
                }
                R.id.profile_picture_5 -> {
                    profile_picture_new.setImageResource(R.drawable.iconfinder_avatar_373_456325_6415360)
                    chosenPicture = "drawable/iconfinder_avatar_373_456325_6415360.png"
                }
                R.id.profile_picture_6 -> {
                    profile_picture_new.setImageResource(R.drawable.iconfinder_avatar_378_456330_6415368)
                    chosenPicture = "drawable/iconfinder_avatar_378_456330_6415368.png"
                }

            }

    }



}


