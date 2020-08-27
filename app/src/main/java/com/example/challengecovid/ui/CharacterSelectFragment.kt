package com.example.challengecovid.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.challengecovid.R
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.database.repository.UserRepository
import com.example.challengecovid.model.Gender
import com.example.challengecovid.model.User
import com.example.challengecovid.viewmodels.getViewModel
import com.example.challengecovid.viewmodels.*
import kotlinx.android.synthetic.main.activity_character_select.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.*

class CharacterSelectFragment : Fragment() {
    val db = ChallengeAppDatabase.getInstance(requireContext(), CoroutineScope(Dispatchers.IO))


    val userRepository = UserRepository(db)

    //    val profileViewModel: ProfileViewModel by lazy {
//        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
//    }
    val profileViewModel = getViewModel{ProfileViewModel(userRepository)}



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        char_select_submit_button.setOnClickListener {
            val username = char_select_username_edittext.text.toString()
            profileViewModel.createNewUser(
                User(
                    UUID.randomUUID().toString(),
                    username,
                    Gender.FEMALE,
                    1,
                    0,
                    "iconfinder_avatar_373_456325_6415360",
                    0
                )
            )
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)

    }

//        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
//        val profileViewModel: ProfileViewModel = getViewModel(ProfileViewModel(userRepository))

    }

}
