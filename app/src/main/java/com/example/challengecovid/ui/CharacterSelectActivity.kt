package com.example.challengecovid.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.challengecovid.R
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.database.repository.UserRepository
import com.example.challengecovid.model.Gender
import com.example.challengecovid.model.User
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.activity_character_select.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.*

class CharacterSelectActivity : Activity() {

    val db = ChallengeAppDatabase.getInstance(application, CoroutineScope(Dispatchers.IO))

    val userRepository = UserRepository(db)

    //    val profileViewModel: ProfileViewModel by lazy {
//        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
//    }

    //TODO: Von Nutzer ausgewählter Avatar soll eingepflanzt werden
    val profileViewModel = getViewModel { ProfileViewModel(userRepository) }

//    val profileViewModel: ProfileViewModel by lazy {
//        ViewModelProvider(this, BaseViewModelFactory { UserViewModel(intent.getIntExtra(USER_ID, -1)) }).get(UserViewModel::class.java)
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_select)

        char_select_submit_button.setOnClickListener {
            val username = char_select_username_edittext.text.toString()
//            val gender =

            profileViewModel.createNewUser(
                User(
                    UUID.randomUUID().toString(),
                    username,
                    Gender.FEMALE,
                    level = 1,
                    points = 0,
                    "iconfinder_avatar_373_456325_6415360",
                    dailyStreakCount = 0
                )
            )
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()


        }
    }
}