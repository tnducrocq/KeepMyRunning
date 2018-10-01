package fr.tnducrocq.keepmyrunning.ui.main

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import fr.tnducrocq.keepmyrunning.R
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        // Set up Action Bar
        val navController = host.navController

        setupActionBar(navController)
        setupNavigationMenu(navController)
        setupBottomNavMenu(navController)

        Log.d(TAG, FirebaseAuth.getInstance().currentUser!!.email)

        //bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        setupBottomNavMenu(navController)

        navController.addOnNavigatedListener { _, destination ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }
            Log.d("NavigationActivity", "Navigated to $dest")
        }
    }

    private fun setupBottomNavMenu(navController: NavController) {
        findViewById<BottomNavigationView>(R.id.bottom_nav_view)?.let { bottomNavView ->
            NavigationUI.setupWithNavController(bottomNavView, navController)
        }
    }

    private fun setupNavigationMenu(navController: NavController) {
        findViewById<NavigationView>(R.id.nav_view)?.let { navigationView ->
            NavigationUI.setupWithNavController(navigationView, navController)
        }
    }

    private fun setupActionBar(navController: NavController) {
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Have the NavHelper look for an action or destination matching the menu
        // item id and navigate there if found.
        // Otherwise, bubble up to the parent.
        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(this, R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp() = Navigation.findNavController(this, R.id.my_nav_host_fragment).navigateUp()
}
