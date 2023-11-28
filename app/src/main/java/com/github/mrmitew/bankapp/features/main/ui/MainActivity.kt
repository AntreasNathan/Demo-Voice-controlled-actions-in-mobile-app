package com.github.mrmitew.bankapp.features.main.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
import androidx.navigation.NavController
import com.github.mrmitew.bankapp.features.accounts.repository.internal.RemoteAccountsRepositoryImpl
import java.math.BigDecimal


class MainActivity : AppCompatActivity() {
    // Create an instance of FakeBackendImpl
    private val fakeBackendImpl = FakeBackendImpl()
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
        //handleIntent(intent)
        onNewIntent(intent)



    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // Handle new incoming intents, if any
        handleIntent(intent)
    }


    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {

            //val thingName: String? = intent.getStringExtra("moneyTransferOrigin.provider")
            val transferMode: String? = intent.getStringExtra("transferMode")
            if (transferMode == "http://schema.googleapis.com/ReceiveMoney") {
                 handleCreateMoneyTransferIntent(intent)
            } else {
                handleGetThingIntent(intent)
            }
        }
    }


    //testing
    private fun handleGetThingIntent(intent: Intent?) {

        // Reference to the TextViews
        lateinit var textViewTitle: TextView
        //lateinit var textViewAccountBalance: TextView

        if (intent?.action == Intent.ACTION_VIEW) {
            val accountName : String? = intent.getStringExtra("name")
            val accountBalance = accountName?.let { fakeBackendImpl.getAccountBalanceByName(it) }

            setContentView(R.layout.view_balance_cmd)
            // Initialize the TextView references
            textViewTitle = findViewById(R.id.textViewTitle)
            //textViewAccountBalance = findViewById(R.id.textViewAccountBalance)

            if (accountBalance != null) {
                // Display the account balance
                //showToast("Account Balance for $accountName: $accountBalance")
                textViewTitle.text = "$accountName\n$accountBalance"

            } else {
                //showToast("No account found with the name: $accountName")
                textViewTitle.text = "No account found with the name: $accountName"
            }

        } else {
            showDefaultView()
        }
    }

    private fun handleCreateMoneyTransferIntent(intent: Intent) {
        //val transferMode: String? = intent.getStringExtra("transferMode")
        lateinit var transferTextView: TextView
        setContentView(R.layout.activity_transfer_result)
        transferTextView = findViewById(R.id.transferResultTextView)
        val amountValue: String? = intent.getStringExtra("value")
        val amountCurrency: String? = intent.getStringExtra("currency")
        val originName: String? = intent.getStringExtra("moneyTransferOriginName")

        val destinationName: String? =
            intent.getStringExtra("moneyTransferDestinationName")

//        val originProviderName: String? =
//            intent.getStringExtra("moneyTransferOriginProvidername")
//
//        val destinationProviderName: String? =
//            intent.getStringExtra("moneyTransferDestinationProvidername")

        val accountBalanceOrigin = originName?.let { fakeBackendImpl.getAccountBalanceByName(it) }
        val accountBalanceDestination = destinationName?.let { fakeBackendImpl.getAccountBalanceByName(it) }
        var transferResultMessage = ""

        if (accountBalanceOrigin != null) {
            if(accountBalanceOrigin < BigDecimal(amountValue)){
                transferResultMessage = "Not enough balance in $originName account"
            }
            else{
                transferResultMessage = "The amount of $amountValue has been successfully transferred from $originName to $destinationName"
            }
        }
        else {
            transferResultMessage = "Account not found with the name: $originName"
        }





        // Set the text in the TextView
        transferTextView.text = transferResultMessage
    }

    private fun showDefaultView() {
        // default view
        showToast("Inside Default View")
    }

    //show toast for checking
    private fun showToast(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    override fun onSupportNavigateUp() = findNavController(R.id.main_content).navigateUp()
}