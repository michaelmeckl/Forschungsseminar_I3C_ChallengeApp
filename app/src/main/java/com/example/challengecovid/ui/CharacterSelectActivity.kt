package com.example.challengecovid.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.challengecovid.R
import com.example.challengecovid.Utils
import com.example.challengecovid.database.ChallengeAppDatabase
import com.example.challengecovid.database.repository.UserRepository
import com.example.challengecovid.model.Gender
import com.example.challengecovid.model.User
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.activity_character_select.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import java.util.*


public class CharacterSelectActivity : AppCompatActivity() {

    lateinit var profileViewModel: ProfileViewModel

    public fun CharacterSelectActivity() {

    }



    //    val profileViewModel: ProfileViewModel by lazy {
//        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
//    }

    //TODO: Von Nutzer ausgewählter Avatar soll eingepflanzt werdenit status

//    val profileViewModel: ProfileViewModel by lazy {
//        ViewModelProvider(this, BaseViewModelFactory { UserViewModel(intent.getIntExtra(USER_ID, -1)) }).get(UserViewModel::class.java)
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_select)
        val db = ChallengeAppDatabase.getInstance(this, CoroutineScope(Dispatchers.IO))
        val userRepository = UserRepository(db)
        profileViewModel = getViewModel { ProfileViewModel(userRepository) }

        char_select_submit_button.setOnClickListener {
            val username = char_select_username_edittext.text.toString()
//            val gender =
            val uuid = UUID.randomUUID().toString()
            val prefs = getSharedPreferences(Utils.SHARED_PREFS_NAME, MODE_PRIVATE)
            prefs.edit().putString(Utils.KEY_USER_ID, uuid).apply()



            profileViewModel.createNewUser(
                User(
                    uuid,
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