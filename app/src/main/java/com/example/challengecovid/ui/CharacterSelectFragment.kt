package com.example.challengecovid.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.challengecovid.R
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.database.repository.UserRepository
import com.example.challengecovid.model.Gender
import com.example.challengecovid.model.User
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.fragment_character_selection.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.*

class CharacterSelectFragment : Fragment(), View.OnClickListener{

    lateinit var currentUser: User
    var userID = UUID.randomUUID().toString()

    //private var name = set_name.toString()      //FIXME: AUCH HIER NIEMALS SO MACHEN, DU. HAST. HIER. NOCH. KEINEN. KONTEXT!!
    private lateinit var chosenPicture: String
    private  var resID : Int = 0

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_character_selection, container, false)

        val application: Application = requireNotNull(this.activity).application
        val db = ChallengeAppDatabase.getInstance(application, CoroutineScope(Dispatchers.IO))
        val userRepository = UserRepository(db)

        profileViewModel = getViewModel { ProfileViewModel(userRepository) }

        set_profile_picture_1.setOnClickListener(this)
        set_profile_picture_2.setOnClickListener(this)
        set_profile_picture_3.setOnClickListener(this)
        set_profile_picture_4.setOnClickListener(this)
        set_profile_picture_5.setOnClickListener(this)
        set_profile_picture_6.setOnClickListener(this)

        val name = set_name.toString()

        save_profile.setOnClickListener {

            if(name != "" ){
                currentUser = User(
                    userID,
                    name,
                    Gender.MALE,
                    1,
                    0,
                    chosenPicture,
                    0,

                    )
                profileViewModel.insertNewUser(currentUser)

            } else {
                Toast.makeText(requireActivity(), "Gebe bitte einen Namen ein", Toast.LENGTH_SHORT).show()
            }





        }


        return root
    }

    override fun onClick(v: View?){
        when(v!!.id){
            R.id.profile_picture_1 -> {

                chosenPicture = "ic_user_man_1"
                resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                set_profile_picture.setImageResource(resID)

            }
            R.id.profile_picture_2 -> {
                chosenPicture = "ic_user_man_2"
                resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                set_profile_picture.setImageResource(resID)


            }
            R.id.profile_picture_3 -> {

                chosenPicture = "ic_user_woman_1"
                resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                set_profile_picture.setImageResource(resID)
            }
            R.id.profile_picture_4 -> {

                chosenPicture = "ic_user_woman_2"
                resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                set_profile_picture.setImageResource(resID)
            }
            R.id.profile_picture_5 -> {

                chosenPicture = "ic_user_woman_3"
                resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                set_profile_picture.setImageResource(resID)
            }
            R.id.profile_picture_6 -> {

                chosenPicture = "ic_user_man_3"
                resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                set_profile_picture.setImageResource(resID)
            }

        }

    }





}

