package com.cito.youoweme

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cito.youoweme.data.sql_database.ContactsSQLiteDAO
import com.cito.youoweme.data.sql_database.TransactionsSQLiteDAO
import com.cito.youoweme.login.UserLoginManager

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewToReturn = inflater.inflate(R.layout.fragment_settings, container, false)

        viewToReturn.findViewById<TextView>(R.id.line_settings_username_txtview).text = UserLoginManager.loggedUser?.username ?: "guest"

        val toClick = viewToReturn.findViewById<Button>(R.id.line_settings_profile_btn)

        toClick.setOnClickListener {
            UserLoginManager(requireContext()).logout()
            TransactionsSQLiteDAO.close()
            ContactsSQLiteDAO.close()
            activity?.finish()
            startActivity(Intent(viewToReturn.context, LoginActivity::class.java))
        }

        return viewToReturn
    }

}