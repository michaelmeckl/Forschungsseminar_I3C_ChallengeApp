package com.example.challengecovid.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.challengecovid.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val navController: NavController by lazy { setupNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup custom toolbar
        setupToolbar()

        // setup bottom navigation with the nav controller
        bottom_nav_view.setupWithNavController(navController)
    }

    private fun setupToolbar() {
        setSupportActionBar(custom_toolbar)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}