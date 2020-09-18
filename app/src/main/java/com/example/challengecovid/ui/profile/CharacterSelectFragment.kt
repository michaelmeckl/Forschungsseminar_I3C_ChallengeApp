package com.example.challengecovid.ui.profile

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.challengecovid.App
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.ProfileViewModelFactory
import kotlinx.android.synthetic.main.fragment_character_selection.*


class CharacterSelectFragment : DialogFragment(), View.OnClickListener {

    private var chosenPicture: String = "ic_user_icon_default"
    private lateinit var profileViewModel: ProfileViewModel

    private var secondCharUnlocked = false
    private var thirdCharUnlocked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.dialogFullScreen)    // set a custom style to make the dialog fragment bigger
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_character_selection, container, false)

        val userRepository = RepositoryController.getUserRepository()
        val application = requireNotNull(this.activity).application
        val store = App.instance
        profileViewModel = ViewModelProvider(store, ProfileViewModelFactory(userRepository, application)).get()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //show current user icon
        chosenPicture = profileViewModel.currentUser.value?.userIcon ?: "ic_user_icon_default"
        val resID = resources.getIdentifier(chosenPicture, "drawable", activity?.packageName)
        set_profile_picture.setImageResource(resID)

        // setup click listeners
        set_profile_picture_1.setOnClickListener(this)
        set_profile_picture_2.setOnClickListener(this)
        set_profile_picture_3.setOnClickListener(this)
        set_profile_picture_4.setOnClickListener(this)
        set_profile_picture_5.setOnClickListener(this)
        set_profile_picture_6.setOnClickListener(this)

        lockAvatars()

        save_profile.setOnClickListener {
            profileViewModel.updateUserIcon(chosenPicture)
            dismiss()
        }

        setupObservers()

    }

    private fun lockAvatars() {
        val grayscaleMatrix = ColorMatrix()
        grayscaleMatrix.setSaturation(0f)

        for (image in listOf(
            set_profile_picture_2,
            set_profile_picture_3,
            set_profile_picture_5,
            set_profile_picture_6
        )) {
            // grey out the image and and set a lock drawable in the foreground
            image.colorFilter = ColorMatrixColorFilter(grayscaleMatrix)
            image.foreground = ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_24, null)
        }
    }

    private fun unlockAvatars(vararg views: ImageButton) {
        val grayscaleMatrix = ColorMatrix()
        grayscaleMatrix.setSaturation(1f)

        for (view in views) {
            view.colorFilter = ColorMatrixColorFilter(grayscaleMatrix)
            view.foreground = null
        }
    }

    private fun setupObservers() {
        profileViewModel.currentUser.observe(viewLifecycleOwner, {
            val currentUser = it ?: return@observe

            if (currentUser.level >= thirdCharUnlockedLevel) {
                thirdCharUnlocked = true
                unlockAvatars(set_profile_picture_2, set_profile_picture_3, set_profile_picture_5, set_profile_picture_6)

            } else if (currentUser.level >= secondCharUnlockedLevel) {
                secondCharUnlocked = true
                unlockAvatars(set_profile_picture_2, set_profile_picture_5)
            }
        })
    }

    override fun onClick(v: View?) {
        v ?: return

        // play a little animation on icon click
        YoYo.with(Techniques.Pulse)
            .duration(150)  // play for 150 ms
            .playOn(v)

        when {
            thirdCharUnlocked -> {

                when (v.id) {
                    R.id.set_profile_picture_1 -> chosenPicture = "ic_user_man_1"
                    R.id.set_profile_picture_2 -> chosenPicture = "ic_user_man_2"
                    R.id.set_profile_picture_3 -> chosenPicture = "ic_user_man_3"
                    R.id.set_profile_picture_4 -> chosenPicture = "ic_user_woman_1"
                    R.id.set_profile_picture_5 -> chosenPicture = "ic_user_woman_2"
                    R.id.set_profile_picture_6 -> chosenPicture = "ic_user_woman_3"
                }
            }
            secondCharUnlocked -> {

                when (v.id) {
                    R.id.set_profile_picture_1 -> chosenPicture = "ic_user_man_1"
                    R.id.set_profile_picture_2 -> chosenPicture = "ic_user_man_2"
                    R.id.set_profile_picture_3 -> showNotYetUnlockedMessage(10)
                    R.id.set_profile_picture_4 -> chosenPicture = "ic_user_woman_1"
                    R.id.set_profile_picture_5 -> chosenPicture = "ic_user_woman_2"
                    R.id.set_profile_picture_6 -> showNotYetUnlockedMessage(10)
                }
            }
            else -> {
                when (v.id) {
                    R.id.set_profile_picture_1 -> chosenPicture = "ic_user_man_1"
                    R.id.set_profile_picture_2 -> showNotYetUnlockedMessage(5)
                    R.id.set_profile_picture_3 -> showNotYetUnlockedMessage(10)
                    R.id.set_profile_picture_4 -> chosenPicture = "ic_user_woman_1"
                    R.id.set_profile_picture_5 -> showNotYetUnlockedMessage(5)
                    R.id.set_profile_picture_6 -> showNotYetUnlockedMessage(10)
                }

            }
        }

        val resID = resources.getIdentifier(chosenPicture, "drawable", activity?.packageName)
        set_profile_picture.setImageResource(resID)
    }


    private fun showNotYetUnlockedMessage(lvl: Int) {
        val toast = Toast.makeText(
            requireActivity(),
            "Dieser Avatar wird erst auf Level $lvl freigeschaltet!",
            Toast.LENGTH_SHORT
        )
        toast.view.setBackgroundColor(resources.getColor(R.color.colorAccent, null))
        toast.view.setPadding(12, 4, 12, 4)
        toast.view.minimumWidth = 850
        toast.view.minimumHeight = 150
        toast.show()
    }

    companion object {
        private const val secondCharUnlockedLevel = 5
        private const val thirdCharUnlockedLevel = 10
    }
}

