package com.example.challengecovid.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.challengecovid.App
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.model.User
import com.example.challengecovid.ui.profile.CharacterSelectFragment
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.ProfileViewModelFactory
import kotlinx.android.synthetic.main.activity_character_creation.*
import kotlinx.coroutines.runBlocking
import timber.log.Timber


class CharacterCreationActivity : AppCompatActivity() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_creation)

        Timber.tag("FIREBASE").d("in onCreate in character creation activity")

        val userRepository = RepositoryController.getUserRepository()
        val app = App.instance
        profileViewModel = ViewModelProvider(app, ProfileViewModelFactory(userRepository, app)).get()
        //profileViewModel = getViewModel { ProfileViewModel(userRepository, application) }

        createDefaultUser()

        profile_image.setOnClickListener {
            startCharacterSelection()
        }

        finish_character_creation_btn.setOnClickListener {
            updateName()
            navigateToMain()

        }
    }

    private fun createDefaultUser() {
        val defaultImage = R.drawable.ic_user_icon_default
        val imagePath = App.instance.resources.getResourceEntryName(defaultImage)

        val newUser = User(
            registrationToken = "TODO",      //TODO: get registration token
            username = getString(R.string.username_placeholder),
            userIcon = imagePath
        )

        // set a default image
        profile_image.setImageResource(defaultImage)

        // save the user in firestore and wait for it to finish before continuing
        // this is important as otherwise there would occur a race condition between inserting and requesting it in the viewmodel observer
        runBlocking {
            profileViewModel.saveNewUser(newUser)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        profileViewModel.currentUser.observe(this, {
            val iconName = it.userIcon
            profile_image.setImageResource(resources.getIdentifier(iconName, "drawable", packageName))
        })
    }

    private fun updateName() {
        if (username_edit_field.text.toString() != "") {
            val username = username_edit_field.text.toString()
            profileViewModel.updateUserName(username)
        }
    }

    private fun startCharacterSelection() {
        val fragment = CharacterSelectFragment()
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