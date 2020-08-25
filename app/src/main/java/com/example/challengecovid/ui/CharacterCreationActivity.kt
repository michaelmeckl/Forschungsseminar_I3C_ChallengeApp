package com.example.challengecovid.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.challengecovid.App
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.model.firebase_model.User_Firebase
import kotlinx.android.synthetic.main.activity_character_creation.*


class CharacterCreationActivity : AppCompatActivity() {

    private val userRepo = RepositoryController.getUserRepository()

    private var imageResource = R.drawable.iconfinder_avatar_368_456320_6415359

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_creation)

        profile_image.setImageResource(R.drawable.iconfinder_avatar_368_456320_6415359)     // set a default image
        profile_image.setOnClickListener {
            //TODO: show the icon selection fragment here and return the R.id of the chosen image (s. oben)!
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

        val newUser = User_Firebase(
            username,
            0,
            0,
            imagePath,
            1,
            emptyList()
        )

        // save the user in firestore
        userRepo.saveNewUser(newUser)
    }


    private fun navigateToMain() {
        val intent = Intent(this@CharacterCreationActivity, MainActivity::class.java)
        startActivity(intent)

        finish()
    }
}