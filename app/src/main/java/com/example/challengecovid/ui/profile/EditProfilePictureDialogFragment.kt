package com.example.challengecovid.ui.profile

import android.app.Application
import android.graphics.Bitmap
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
import com.example.challengecovid.model.User
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.popup_edit_profile_picture.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.IOException
import java.nio.channels.AsynchronousFileChannel.open
import kotlin.properties.Delegates


class EditProfilePictureDialogFragment: DialogFragment(), View.OnClickListener {

    private lateinit var chosenPicture: String

    private  var resID : Int = 0

    private lateinit var profileViewModel: ProfileViewModel

    //var profile = CharacterSelectActivity()
    //private var currentUserId = profile.currentUser.userId
    private lateinit var currentUser: User


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

            currentUser = profileViewModel.getUser("test tes test") ?: return@setOnClickListener    //TODO
            currentUser.userIcon = chosenPicture

            profileViewModel.updateUser(currentUser)

            requireActivity().findNavController(R.id.nav_host_fragment).popBackStack()
        }

    }

    override fun onClick(v: View?){
            when(v!!.id){
                R.id.profile_picture_1 -> {

                    chosenPicture = "ic_user_man_1"
                    resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                    profile_picture_new.setImageResource(resID)

                }
                R.id.profile_picture_2 -> {
                    chosenPicture = "ic_user_man_2"
                    resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                    profile_picture_new.setImageResource(resID)


                }
                R.id.profile_picture_3 -> {

                    chosenPicture = "ic_user_woman_1"
                    resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                    profile_picture_new.setImageResource(resID)
                }
                R.id.profile_picture_4 -> {

                    chosenPicture = "ic_user_woman_2"
                    resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                    profile_picture_new.setImageResource(resID)
                }
                R.id.profile_picture_5 -> {

                    chosenPicture = "ic_user_woman_3"
                    resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                    profile_picture_new.setImageResource(resID)
                }
                R.id.profile_picture_6 -> {

                    chosenPicture = "ic_user_man_3"
                    resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                    profile_picture_new.setImageResource(resID)
                }

            }

    }





}


