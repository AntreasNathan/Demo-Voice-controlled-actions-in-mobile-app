package com.github.mrmitew.bankapp.features.login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.mrmitew.bankapp.R

class ViewBalanceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.view_balance_cmd, container, false)

        // Find the button by its ID
        // Set a click listener for the button
        view.findViewById<Button>(R.id.btn_back).setOnClickListener {
            // Navigate to the specified destination when the button is clicked
            findNavController().navigate(R.id.accountList)
        }

        return view
    }
}
