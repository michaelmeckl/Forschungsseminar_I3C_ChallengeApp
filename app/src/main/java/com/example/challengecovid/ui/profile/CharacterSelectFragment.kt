package com.example.challengecovid.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import androidx.navigation.findNavController
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.ProfileViewModelFactory
import com.example.challengecovid.viewmodels.getViewModel
import kotlinx.android.synthetic.main.fragment_character_selection.*

class CharacterSelectFragment : DialogFragment(), View.OnClickListener {

    private var chosenPicture: String = "ic_user_icon_default"
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.dialogFullScreen)    // set a custom style to make the dialog fragment bigger
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_character_selection, container, false)

        val userRepository = RepositoryController.getUserRepository()
        val application = requireNotNull(this.activity).application
        val store: ViewModelStoreOwner =
            requireActivity().findNavController(R.id.nav_host_fragment).getViewModelStoreOwner(R.id.profile_graph)
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

        save_profile.setOnClickListener {
            profileViewModel.updateUserIcon(chosenPicture)
            dismiss()
        }
    }

    private fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && str.isNotEmpty())
            return false
        return true
    }

    override fun onClick(v: View?) {
        v ?: return

        when (v.id) {
            R.id.set_profile_picture_1 -> chosenPicture = "ic_user_man_1"
            R.id.set_profile_picture_2 -> chosenPicture = "ic_user_man_2"
            R.id.set_profile_picture_3 -> chosenPicture = "ic_user_man_3"
            R.id.set_profile_picture_4 -> chosenPicture = "ic_user_woman_1"
            R.id.set_profile_picture_5 -> chosenPicture = "ic_user_woman_2"
            R.id.set_profile_picture_6 -> chosenPicture = "ic_user_woman_3"
        }

        val resID = resources.getIdentifier(chosenPicture, "drawable", activity?.packageName)
        set_profile_picture.setImageResource(resID)

    }
}

