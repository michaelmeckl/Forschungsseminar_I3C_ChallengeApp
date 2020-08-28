package com.example.challengecovid.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.challengecovid.App
import com.example.challengecovid.Constants
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.model.User
import com.example.challengecovid.ui.profile.CharacterSelectFragment
import kotlinx.android.synthetic.main.activity_character_creation.*
import timber.log.Timber


class CharacterCreationActivity : AppCompatActivity() {

    private val userRepo = RepositoryController.getUserRepository()

    private var imageResource = R.drawable.iconfinder_avatar_368_456320_6415359

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_creation)

        Timber.tag("FIREBASE").d("in onCreate in character creation activity")

        profile_image.setImageResource(R.drawable.iconfinder_avatar_368_456320_6415359)     // set a default image
        profile_image.setOnClickListener {
            //TODO: return the R.id of the chosen image (s. oben)! -> maybe use the profile viewmodel for this?
            startCharacterSelection()
        }

        finish_character_creation_btn.setOnClickListener {
            saveUserInDB()
            navigateToMain()
        }
    }

    private fun saveUserInDB() {
        val imagePath = App.instance.resources.getResourceEntryName(imageResource)

        val username = if (username_edit_field.text.toString() == "")
            "Anonym"
        else
            username_edit_field.text.toString()

        val newUser = User(
            registrationToken = "hk57gds",      //TODO: get registration token
            username = username,
            userIcon = imagePath
        )

        // save the user in firestore
        val userId = userRepo.saveNewUser(newUser)

        // store the generated userId in the shared prefs to be able to access this user later
        val sharedPrefs = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE)
        sharedPrefs.edit().putString(Constants.PREFS_USER_ID, userId).apply()
    }

    // TODO navigate to Character Selection
    private fun startCharacterSelection() {
        val fragment = CharacterSelectFragment()
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment)        //TODO: to the android content? create a fragment container instead!
            .commit()
    }

    private fun navigateToMain() {
        val intent = Intent(this@CharacterCreationActivity, MainActivity::class.java)
        startActivity(intent)

        finish()
    }

    override fun finish() {
        super.finish()
        // use a custom transition when this activity finishes
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}