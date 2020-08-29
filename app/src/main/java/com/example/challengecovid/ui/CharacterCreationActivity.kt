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
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.activity_character_creation.*
import timber.log.Timber


class CharacterCreationActivity : AppCompatActivity() {

    private val userRepository = RepositoryController.getUserRepository()    //TODO: bad code design, shouldn't be accessed here
    private lateinit var profileViewModel: ProfileViewModel

    private var imageResource = R.drawable.ic_user_icon_default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_creation)

        Timber.tag("FIREBASE").d("in onCreate in character creation activity")
        val userId = createUser()

        val userRepository = RepositoryController.getUserRepository()
        profileViewModel = getViewModel { ProfileViewModel(userRepository, application) }

        profileViewModel.observeImage().observe(this, { it ->
            if (it != null) {
                //TODO: this should be in utils as its used quite often!
                imageResource = resources.getIdentifier(it, "drawable", packageName)
            }
        })

        profile_image.setImageResource(imageResource)     // set a default image
        profile_image.setOnClickListener {
            startCharacterSelection()
        }

        finish_character_creation_btn.setOnClickListener {
            updateName()
            navigateToMain()
        }
    }

    private fun createUser(): String? {
        val imagePath = App.instance.resources.getResourceEntryName(imageResource)
        val newUser = User(
            registrationToken = "TODO",      //TODO: get registration token
            username = getString(R.string.username_placeholder),
            userIcon = imagePath
        )

        // save the user in firestore
        val userId = userRepository.saveNewUser(newUser)

        // store the generated userId in the shared prefs to be able to access this user later
        val sharedPrefs = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE)
        sharedPrefs.edit().putString(Constants.PREFS_USER_ID, userId).apply()

        return userId
    }

    private fun updateName() {
        if (username_edit_field.text.toString() != "") {
            val username = username_edit_field.text.toString()
            profileViewModel.updateUserName(username)
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