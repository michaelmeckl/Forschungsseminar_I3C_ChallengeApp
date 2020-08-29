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

    //TODO: eigentlich sollte hier eher das viemodel und nicht das repo verwendet werden!
    private val userRepo = RepositoryController.getUserRepository()

    private var imageResource = R.drawable.iconfinder_avatar_368_456320_6415359

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_creation)

        Timber.tag("FIREBASE").d("in onCreate in character creation activity")
        val userId = createUser()

        profile_image.setImageResource(imageResource)     // set a default image
        profile_image.setOnClickListener {
            //TODO: return the R.id of the chosen image (s. oben)! -> maybe use the profile viewmodel for this?
            startCharacterSelection()
        }

        finish_character_creation_btn.setOnClickListener {
            updateName(userId)
            navigateToMain()
        }
    }

    private fun createUser(): String {
        val imagePath = App.instance.resources.getResourceEntryName(imageResource)
        val newUser = User(
            registrationToken = "hk57gds",      //TODO: get registration token
            username = "Anonym",
            userIcon = imagePath
        )

        // save the user in firestore
        val userId = userRepo.saveNewUser(newUser)

        // store the generated userId in the shared prefs to be able to access this user later
        val sharedPrefs = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE)
        sharedPrefs.edit().putString(Constants.PREFS_USER_ID, userId).apply()

        return userId
    }

    private fun updateName(userId: String) {
        if (username_edit_field.text.toString() != "") {
            val username = username_edit_field.text.toString()

            userRepo.updateUserName(username, userId)
        }
    }

    private fun startCharacterSelection() {
        val fragment = CharacterSelectFragment()
        //TODO: nav controller geht hier noch nicht!!!!
        /*
        supportFragmentManager.beginTransaction()
            .add(R.id.character_select_container, fragment)
            .addToBackStack(null)
            .commit()

         */
        fragment.show(supportFragmentManager, null)
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