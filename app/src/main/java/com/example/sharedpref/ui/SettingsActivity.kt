package com.example.sharedpref.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.sharedpref.R
import com.example.sharedpref.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_settings.*

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        lifecycleScope.launchWhenResumed {
            viewModel.updateSettingsLiveData.observe(this@SettingsActivity, Observer {
                if (it) {
                    startActivity(Intent(this@SettingsActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                } else {
                    Toast.makeText(
                        this@SettingsActivity,
                        "Error while updaing settings",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            viewModel.showSeletedSettings.observe(this@SettingsActivity, Observer {
                showSettingsCB.isChecked = it.showSettings
                darkRB.isChecked = it.isDark
            })

            updateSettingsBtn.setOnClickListener {
                val showSetting = showSettingsCB.isChecked
                val isDark = themeRadioGroup.checkedRadioButtonId == R.id.darkRB
                viewModel.update(showSetting, isDark)
            }
        }
    }

    override fun onResume() {
        viewModel.showSelected()
        super.onResume()
    }
}