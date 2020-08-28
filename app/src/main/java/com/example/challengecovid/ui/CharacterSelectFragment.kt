package com.example.challengecovid.ui

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
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
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs

class CharacterSelectFragment : Fragment(), View.OnClickListener{


    var userID = UUID.randomUUID().toString()

    //private var name = set_name.toString()      //FIXME: AUCH HIER NIEMALS SO MACHEN, DU. HAST. HIER. NOCH. KEINEN. KONTEXT!!
    private lateinit var chosenPicture: String
    private  var resID : Int = 0

    private val args: CharacterSelectFragmentArgs by navArgs()

    private lateinit var firstRun: String

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_character_selection, container, false)

        val application: Application = requireNotNull(this.activity).application
        val db = ChallengeAppDatabase.getInstance(application, CoroutineScope(Dispatchers.IO))
        val userRepository = UserRepository(db)

        profileViewModel = getViewModel { ProfileViewModel(userRepository) }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        set_profile_picture_1.setOnClickListener(this)
        set_profile_picture_2.setOnClickListener(this)
        set_profile_picture_3.setOnClickListener(this)
        set_profile_picture_4.setOnClickListener(this)
        set_profile_picture_5.setOnClickListener(this)
        set_profile_picture_6.setOnClickListener(this)

        save_profile.setOnClickListener {
            firstRun = args.firstRun
            val name = set_name.toString()

            if(isNullOrEmpty(name)){
                showEnterNameMessage()
            } else{
                if(firstRun == null){
                    insertUser(name)
                    /*
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)

                     */

                    requireActivity().findNavController(R.id.nav_host_fragment).popBackStack()
                }
                else {
                    updateUser(name)
                    requireActivity().findNavController(R.id.nav_host_fragment).popBackStack()
                }


            }

        }


    }

    private fun showEnterNameMessage(){
        Toast.makeText(requireActivity(), "Gebe bitte einen Namen ein", Toast.LENGTH_SHORT).show()
    }

    private fun insertUser(name:String){
        val newUser = User(
            userID,
            name,
            Gender.MALE,
            1,
            0,
            chosenPicture,
            0,

            )
        profileViewModel.insertNewUser(newUser)
    }


    private fun updateUser(name:String){
        val currentUser = profileViewModel.getUser("test")

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

    override fun onClick(v: View?){
        when(v!!.id){
            R.id.set_profile_picture_1 -> {

                chosenPicture = "ic_user_man_1"
                resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                set_profile_picture.setImageResource(resID)

            }
            R.id.set_profile_picture_2 -> {
                chosenPicture = "ic_user_man_2"
                resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                set_profile_picture.setImageResource(resID)


            }
            R.id.set_profile_picture_3 -> {

                chosenPicture = "ic_user_woman_1"
                resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                set_profile_picture.setImageResource(resID)
            }
            R.id.set_profile_picture_4 -> {

                chosenPicture = "ic_user_woman_2"
                resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                set_profile_picture.setImageResource(resID)
            }
            R.id.set_profile_picture_5 -> {

                chosenPicture = "ic_user_woman_3"
                resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                set_profile_picture.setImageResource(resID)
            }
            R.id.set_profile_picture_6 -> {

                chosenPicture = "ic_user_man_3"
                resID = resources.getIdentifier(chosenPicture, "drawable", "com.example.challengecovid")
                set_profile_picture.setImageResource(resID)
            }

        }

    }





}

