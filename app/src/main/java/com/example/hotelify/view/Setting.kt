package com.example.hotelify.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceActivity
import android.widget.Toast
import androidx.preference.SwitchPreferenceCompat
import com.example.hotelify.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_task_show.*

class Setting : PreferenceActivity(){
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        addPreferencesFromResource(R.xml.setting)
        val darkThemePreference  = findPreference("darkTheme")
    }
}
