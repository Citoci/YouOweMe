package com.cito.youoweme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.data.sql_database.TransactionsSQLiteDAO
import com.cito.youoweme.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        TransactionsSQLiteDAO.open(this)
        ContactsSQLiteDAO.open(this)

//        TransactionsSQLiteDAO.insert(Transaction(amount = 40f))

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main) // the host fragment (works only with fragment xml tag)
//        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment).navController

        // This is just to put titles in the ActionBar on the top
        setupActionBarWithNavController(navController, AppBarConfiguration(
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            setOf(
                R.id.navigation_transactions_list, R.id.navigation_per_contact_balance, R.id.navigation_settings
            )
        ))

        navView.setupWithNavController(navController)

//        if (LoggedUser.username == null)
//            navView.menu.findItem(R.id.navigation_settings).run {
////                isEnabled = false
////                icon.alpha = 250
////                isVisible = false
//            }
    }

    override fun onDestroy() {
        TransactionsSQLiteDAO.close()
        ContactsSQLiteDAO.close()
        super.onDestroy()
    }
}