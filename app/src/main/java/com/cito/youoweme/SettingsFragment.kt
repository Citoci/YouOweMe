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

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewToReturn = inflater.inflate(R.layout.fragment_settings, container, false)

        with (viewToReturn.findViewById<TextView>(R.id.profile_username_text)) {
            append(" \"" + (UserLoginManager.loggedUser?.username ?: "guest") + "\"")
        }

        with (viewToReturn.findViewById<Button>(R.id.btn_logout)) {
            setOnClickListener {
                UserLoginManager(requireContext()).logout()
                TransactionsSQLiteDAO.close()
                ContactsSQLiteDAO.close()
                activity?.finish()
                startActivity(Intent(viewToReturn.context, LoginActivity::class.java))
            }
        }

        return viewToReturn
    }

}