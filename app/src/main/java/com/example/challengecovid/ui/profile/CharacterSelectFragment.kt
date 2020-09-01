package com.example.challengecovid.ui.profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.challengecovid.App
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.model.User
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.ProfileViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_character_selection.*
import timber.log.Timber

class CharacterSelectFragment : DialogFragment(), View.OnClickListener {

    private var chosenPicture: String = "ic_user_icon_default"
    private lateinit var profileViewModel: ProfileViewModel

    private lateinit var _currentUser: User
    private var secondCharUnlocked = false
    private var thirdCharUnlocked = false

    private val secondCharUnlockedLevel = 5
    private val thirdCharUnlockedLevel = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.dialogFullScreen)    // set a custom style to make the dialog fragment bigger
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_character_selection, container, false)

        val userRepository = RepositoryController.getUserRepository()
        val application = requireNotNull(this.activity).application
        /*
        val store = requireActivity().findNavController(R.id.nav_host_fragment).getViewModelStoreOwner(R.id.profile_graph)
        profileViewModel = ViewModelProvider(store, ProfileViewModelFactory(userRepository, application)).get()
        */
        val store = App.instance
        profileViewModel = ViewModelProvider(store, ProfileViewModelFactory(userRepository, application)).get()

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
        set_profile_picture_2.imageAlpha = 75
        set_profile_picture_3.imageAlpha = 75
        set_profile_picture_5.imageAlpha = 75
        set_profile_picture_6.imageAlpha = 75


        save_profile.setOnClickListener {
            profileViewModel.updateUserIcon(chosenPicture)
            dismiss()
        }

        setupObservers()

    }

    private fun setupObservers() {
        profileViewModel.currentUser.observe(viewLifecycleOwner, { it ->
            it?.let {
                _currentUser = it

                if (_currentUser.level >= thirdCharUnlockedLevel) {
                    thirdCharUnlocked = true
                    set_profile_picture_2.imageAlpha = 255
                    set_profile_picture_3.imageAlpha = 255
                    set_profile_picture_5.imageAlpha = 255
                    set_profile_picture_6.imageAlpha = 255

                } else if (_currentUser.level >= secondCharUnlockedLevel) {
                    secondCharUnlocked = true
                    set_profile_picture_2.imageAlpha = 255
                    set_profile_picture_5.imageAlpha = 255

                }

            }
        })
    }

    /*
    private fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && str.isNotEmpty())
            return false
        return true
    }*/

    override fun onClick(v: View?) {
        v ?: return

        if (thirdCharUnlocked) {

            when (v.id) {
                R.id.set_profile_picture_1 -> chosenPicture = "ic_user_man_1"
                R.id.set_profile_picture_2 -> chosenPicture = "ic_user_man_2"
                R.id.set_profile_picture_3 -> chosenPicture = "ic_user_man_3"
                R.id.set_profile_picture_4 -> chosenPicture = "ic_user_woman_1"
                R.id.set_profile_picture_5 -> chosenPicture = "ic_user_woman_2"
                R.id.set_profile_picture_6 -> chosenPicture = "ic_user_woman_3"
            }
        } else if (secondCharUnlocked) {

            when (v.id) {
                R.id.set_profile_picture_1 -> chosenPicture = "ic_user_man_1"
                R.id.set_profile_picture_2 -> chosenPicture = "ic_user_man_2"
                R.id.set_profile_picture_3 -> Toast.makeText(requireContext(),"Wird erst ab Level 10 freigeschaltet", Toast.LENGTH_SHORT).show()
                R.id.set_profile_picture_4 -> chosenPicture = "ic_user_woman_1"
                R.id.set_profile_picture_5 -> chosenPicture = "ic_user_woman_2"
                R.id.set_profile_picture_6 -> Toast.makeText(requireContext(),"Wird erst ab Level 10 freigeschaltet", Toast.LENGTH_SHORT).show()
            }
        } else {
            when (v.id) {
                R.id.set_profile_picture_1 -> chosenPicture = "ic_user_man_1"
                R.id.set_profile_picture_2 -> Toast.makeText(requireContext(),"Wird erst ab Level 5 freigeschaltet", Toast.LENGTH_SHORT).show()
                R.id.set_profile_picture_3 -> Toast.makeText(requireContext(),"Wird erst ab Level 10 freigeschaltet", Toast.LENGTH_SHORT).show()
                R.id.set_profile_picture_4 -> chosenPicture = "ic_user_woman_1"
                R.id.set_profile_picture_5 -> Toast.makeText(requireContext(),"Wird erst ab Level 5 freigeschaltet", Toast.LENGTH_SHORT).show()
                R.id.set_profile_picture_6 -> Toast.makeText(requireContext(),"Wird erst ab Level 10 freigeschaltet", Toast.LENGTH_SHORT).show()
            }

        }

        val resID = resources.getIdentifier(chosenPicture, "drawable", activity?.packageName)
        set_profile_picture.setImageResource(resID)
    }

    // TODO: Wenn Zeit übrig: Snackbar statt toasts, da schicker. SB Führen aber hier noch zu Crash (nur bei initialer Char-erstellung)
    private fun showSnackbar(lvl: Int) {
        Snackbar.make(
            requireActivity().findViewById(R.id.container),
            "Dieser Avatar wird erst ab Level " + lvl.toString() + " freigeschaltet",
            Snackbar.LENGTH_SHORT
        ).show()
    }
}

