package com.cito.youoweme

import android.content.Intent
import android.graphics.Color
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
import com.cito.youoweme.login.model.User

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragView = inflater.inflate(
            if (UserLoginManager.loggedUser != null) R.layout.fragment_settings else R.layout.fragment_settings_guest,
            container, false
        )

        with (fragView.findViewById<TextView>(R.id.profile_username_text)) {
            this?.append(" \"" + (UserLoginManager.loggedUser?.username ?: "guest") + "\"")
        }

        with (fragView.findViewById<Button>(R.id.btn_logout)) {
            this?.setOnClickListener {
                UserLoginManager(requireContext()).logout()
                TransactionsSQLiteDAO.close()
                ContactsSQLiteDAO.close()
                activity?.finish()
                startActivity(Intent(fragView.context, LoginActivity::class.java))
            }
        }

        return fragView
    }

}