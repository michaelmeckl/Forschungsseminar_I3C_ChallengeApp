package com.example.coronasimulation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup a custom toolbar
        setupToolbar()

        // setup the nav controller
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = getNavController()
        navView.setupWithNavController(navController)
    }

    private fun setupToolbar() {
        setSupportActionBar(custom_toolbar)

        // customize toolbar
    }

    // https://stackoverflow.com/questions/59275009/fragmentcontainerview-using-findnavcontroller
    // workaround for https://issuetracker.google.com/issues/142847973
    private fun getNavController(): NavController {
        val navFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        check(navFragment is NavHostFragment) {
            ("Activity $this does not have a NavHostFragment")
        }
        return (navFragment as NavHostFragment?)!!.navController
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            //TODO
            R.id.action_profile -> true
            R.id.action_about -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}