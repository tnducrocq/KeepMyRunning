package fr.tnducrocq.keepmyrunning.ui.running

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import fr.tnducrocq.keepmyrunning.R
import kotlinx.android.synthetic.main.main_activity.*


class RunningActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_activity)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        navController = host.navController
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (navController.currentDestination?.id == R.id.navigation_new_running && item.itemId == android.R.id.home) {
            finish()
            return true
        }

        // Have the NavHelper look for an action or destination matching the menu
        // item id and navigate there if found.
        // Otherwise, bubble up to the parent.
        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(this, R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp() = Navigation.findNavController(this, R.id.my_nav_host_fragment).navigateUp()
}
