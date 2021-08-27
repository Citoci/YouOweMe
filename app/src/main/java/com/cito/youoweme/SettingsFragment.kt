package com.cito.youoweme

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.cito.youoweme.login.UserLoginManager

class SettingsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragView = inflater.inflate(
            if (UserLoginManager.isLogged) R.layout.fragment_settings else R.layout.fragment_settings_guest,
            container, false
        )

        with(fragView.findViewById<TextView>(R.id.profile_username_text)) {
            this?.append(" \"" + (UserLoginManager.loggedUser?.username ?: "guest") + "\"")
        }

        with(fragView.findViewById<Button>(R.id.btn_logout)) {
            this?.setOnClickListener {
                UserLoginManager(requireContext()).logout()
//                TransactionsSQLiteDAO.close()
//                ContactsSQLiteDAO.close()
                activity?.finish()
                startActivity(Intent(fragView.context, LoginActivity::class.java))
            }
        }

        with(fragView.findViewById<Button>(R.id.notification_settings_btn)) {
            this?.setOnClickListener {
                openSystemNotificationSettings()
            }
        }

        return fragView
    }

    private fun openSystemNotificationSettings() {
        startActivity(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context?.packageName)
                }
            } else Intent("android.settings.APP_NOTIFICATION_SETTINGS").apply {
                putExtra("app_package", context?.packageName)
                putExtra("app_uid", context?.applicationInfo?.uid)
            }
        )
    }

}