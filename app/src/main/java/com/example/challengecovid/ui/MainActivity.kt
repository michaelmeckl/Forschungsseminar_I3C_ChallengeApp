package com.example.challengecovid.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.challengecovid.R
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private val navController: NavController by lazy { setupNavController() }
    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            // add top level views here
            setOf(
                R.id.navigation_overview,
                R.id.navigation_categories,
                R.id.navigation_feed
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup custom toolbar
        setupToolbar()

        Timber.tag("FIREBASE").d("in onCreate in main activity")

        // setup bottom navigation with the nav controller
        bottom_nav_view.setupWithNavController(navController)

        /*
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in setOf(R.id.navigation_credits, R.id.navigation_category_detail)) {
                // remove the toolbar logo in the detail view and in the credits
                custom_toolbar.logo = null
            } else {
                //TODO: maybe find some other suitable icon?
                custom_toolbar.setLogo(R.drawable.ic_coronavirus)
            }
        }*/

        //TODO check here if there is a internet connection otherwise inform the user that the app may not run as expected!
    }

    private fun setupToolbar() {
        setSupportActionBar(custom_toolbar as? Toolbar)
        (custom_toolbar as? Toolbar)?.setupWithNavController(navController, appBarConfiguration)
    }

    /**
     * Returns the navigation controller associated with this activity.
     * See https://stackoverflow.com/questions/59275009/fragmentcontainerview-using-findnavcontroller.
     */
    private fun setupNavController(): NavController {
        // This is a workaround for https://issuetracker.google.com/issues/142847973.
        val navFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        check(navFragment is NavHostFragment) {
            ("Activity $this does not have a NavHostFragment")
        }
        return (navFragment as NavHostFragment?)!!.navController
    }

    // Inflate the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Handle clicks on the menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}