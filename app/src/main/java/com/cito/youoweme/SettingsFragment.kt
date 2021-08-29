package com.cito.youoweme

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
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

        fragView.findViewById<TextView>(R.id.profile_username_text)?.apply {
            text = getString(R.string.message_hello_login, UserLoginManager.loggedUsername)
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

        return fragView
    }

    class PreferencesFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.settings_preferences, rootKey)

            findPreference<Preference>("notifications_settings_preference")?.intent =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context?.packageName)
                    }
                } else Intent("android.settings.APP_NOTIFICATION_SETTINGS").apply {
                    putExtra("app_package", context?.packageName)
                    putExtra("app_uid", context?.applicationInfo?.uid)
                }

            findPreference<Preference>("import_contacts_preference")?.intent =
                Intent(activity, AddContactActivity::class.java)
        }
    }

}