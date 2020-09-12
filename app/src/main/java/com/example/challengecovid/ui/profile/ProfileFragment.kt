package com.example.challengecovid.ui.profile

import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.findNavController
import com.example.challengecovid.App
import com.example.challengecovid.Constants
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.ui.OverviewFragment
import com.example.challengecovid.viewmodels.ProfileViewModel
import com.example.challengecovid.viewmodels.ProfileViewModelFactory
import kotlinx.android.synthetic.main.fragment_profile.*
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private val levelsHashMap = OverviewFragment.levelsMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val userRepository = RepositoryController.getUserRepository()
        val application = requireNotNull(this.activity).application
        val store = App.instance
        profileViewModel = ViewModelProvider(store, ProfileViewModelFactory(userRepository, application)).get()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        //showcase view to show user how to change icon!
        MaterialShowcaseView.Builder(requireActivity())
            .setTarget(profile_picture)
            .setDismissText("Verstanden!")
            .setContentText("Du kannst dein Profilbild ganz einfach ändern, indem du darauf klickst!")
            .setMaskColour(ContextCompat.getColor(requireActivity(), R.color.dark_grey_transparent))
            .setDismissOnTouch(false)
            .setDismissOnTargetTouch(true)
            .setDismissTextColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimaryDark))
            .setDismissStyle(Typeface.DEFAULT_BOLD)
            .setTargetTouchable(true)
            .singleUse("abcdefghijk")  // show this showcase only once by providing a unique id
            .setDelay(400)
            .show()

        observeViewModel()

        profile_picture.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileToCharacterSelect()
            requireActivity().findNavController(R.id.nav_host_fragment).navigate(action)
        }

        save_changes.setOnClickListener {
            val newName = change_name_field.text.toString()
            if(newName.isNotBlank()) {
                // only update if he has entered at least one character that is not a whitespace
                profileViewModel.updateUserName(newName)
            }

            // clear the focus of the edit field so it won't be activated the next time the user enters the profile
            change_name_field.clearFocus()

            //remove this fragment from the backstack to navigate back
            requireActivity().findNavController(R.id.nav_host_fragment).popBackStack()
        }

        val sharedPrefs = requireActivity().getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE)

        // Set achievement values in profile fragment
        achievements_total_completed_value.text = sharedPrefs.getInt(Constants.PREFS_COUNT_COMPLETED_CHALLENGES, 0).toString()
        achievements_xp_value.text = sharedPrefs.getInt(Constants.PREFS_COUNT_TOTAL_XP, 0).toString()
        achievements_total_created_value.text = sharedPrefs.getInt(Constants.PREFS_COUNT_CREATED_CHALLENGES, 0).toString()
        achievements_dailystreak_value.text = sharedPrefs.getInt(Constants.PREFS_COUNT_CONSECUTIVE_DAYS, 1).toString()
    }

    private fun observeViewModel() {
        profileViewModel.currentUser.observe(viewLifecycleOwner, {

            // set only the hint instead of the text to prevent two way binding (which wouldn't allow to edit the field anymore)
            //change_name_field.hint = it.username

            profile_name.text = it.username
            profile_level.text = getString(R.string.level, it.level)

//            level_progress.text = getString(R.string.levelProgress,it.points,levelsHashMap[it.level])
            if (it.level == MAX_LEVEL) {
                level_progressbar.max = 100
                level_progressbar.progress = 100
                level_progress.text = "Du hast das maximale Level erreicht!"
            } else {
                val xpForLevel = levelsHashMap[it.level]
                if (xpForLevel != null) {
                    val remainingXPForLevelUp = xpForLevel.minus(it.points)
                    level_progressbar.max = xpForLevel
                    level_progressbar.progress = it.points
                    level_progress.text = "Noch $remainingXPForLevelUp XP bis Level ${it.level + 1}"
                }
            }
            val iconIdentifier =
                requireActivity().resources.getIdentifier(it.userIcon, "drawable", requireActivity().packageName)
            profile_picture.setImageResource(iconIdentifier)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()    // don't show the main menu in this fragment!
    }

    companion object {
        private const val MAX_LEVEL = 20
    }
}