package com.github.mrmitew.bankapp.features.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.mrmitew.bankapp.R
import com.google.android.material.navigation.NavigationView
import android.widget.Toast
import com.github.mrmitew.bankapp.features.accounts.dto.AccountDTO
import com.github.mrmitew.bankapp.features.backend.internal.FakeBackendImpl
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val navController = findNavController(R.id.main_content)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.login, R.id.home, R.id.settings, R.id.accountList),
            drawerLayout
        )

        toolbar.setupWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Don't navigate anywhere, just exit the app.
        // We won't do this in a real app.
        // We'll make sure to erase user settings and then proceed with exiting
        // In fact... this listener will override the previous [navView.setupWithNavController(navController)]
        // But for the demo its fine.
        navView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.logout) {
                onBackPressed()
            }
            false
        }

        //Handle the incoming intent
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // Handle new incoming intents, if any
        handleIntent(intent)
    }


//Working sample
//    private fun handleIntent(intent: Intent?) {
//        if (intent?.action == Intent.ACTION_VIEW) {
//            val thingName : String? = intent.getStringExtra("thing.name")
//
//            if (thingName != null) {
//
//                showToast("AccName: $thingName")
//            } else {
//
//                showToast("Thing name is null")
//            }
//        } else {
//            showDefaultView()
//        }
//    }



    //testing
    private fun handleIntent(intent: Intent?) {


        // Create an instance of FakeBackendImpl
        val fakeBackendImpl = FakeBackendImpl()
        if (intent?.action == Intent.ACTION_VIEW) {
            val accountName : String? = intent.getStringExtra("name")
            //val accountName = thingName
            val accountBalance = accountName?.let { fakeBackendImpl.getAccountBalanceByName(it) }




            if (accountBalance != null) {
                // Display the account balance
                showToast("Account Balance for $accountName: $accountBalance")

            } else {
                showToast("No account found with the name: $accountName")
            }
        } else {
            showDefaultView()
        }
    }

    private fun showDefaultView() {
        // default view
    }

    //show toast for checking
    private fun showToast(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    override fun onSupportNavigateUp() = findNavController(R.id.main_content).navigateUp()
}