package com.cito.youoweme

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.cito.youoweme.R

class PreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_example, rootKey)
    }
}