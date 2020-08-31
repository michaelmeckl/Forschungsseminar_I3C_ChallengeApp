package com.example.challengecovid.ui.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.findNavController
import com.example.challengecovid.App
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.ProfileViewModelFactory
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

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
        setHasOptionsMenu(true)

        observeViewModel()

        profile_picture.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileToCharacterSelect()
            requireActivity().findNavController(R.id.nav_host_fragment).navigate(action)
        }

        save_changes.setOnClickListener {
            val newName = change_name_field.text.toString()
            profileViewModel.updateUserName(newName)

            // clear the focus of the edit field so it won't be activated the next time the user enters the profile
            change_name_field.clearFocus()

            //remove this fragment from the backstack to navigate back
            requireActivity().findNavController(R.id.nav_host_fragment).popBackStack()
        }
    }

    private fun observeViewModel() {
        profileViewModel.currentUser.observe(viewLifecycleOwner, {

            // set only the hint instead of the text to prevent two way binding (which wouldn't allow to edit the field anymore)
            change_name_field.hint = it.username

            profile_name.text = it.username
            profile_level.text = getString(R.string.level, it.level)

            val iconIdentifier =
                requireActivity().resources.getIdentifier(it.userIcon, "drawable", requireActivity().packageName)
            profile_picture.setImageResource(iconIdentifier)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()    // don't show the main menu in this fragment!
    }
}