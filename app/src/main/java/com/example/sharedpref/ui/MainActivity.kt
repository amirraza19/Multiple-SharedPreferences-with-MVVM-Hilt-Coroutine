package com.example.sharedpref.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.sharedpref.R
import com.example.sharedpref.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            viewModel.settingsDataLiveData.observe(this@MainActivity, Observer {
                if (it.showSettings) {
                    showSettingsTV.visibility = View.VISIBLE
                    showSettingsTV.text =
                        "Show Settings: ${it.showSettings}\nTheme: ${if (it.isDark) "Dark" else "Light"}"
                } else {
                    showSettingsTV.visibility = View.GONE
                }
            })
            viewModel.userData.observe(this@MainActivity, Observer {
                loginDetail.text = it
            })
            viewModel.clearAllPreferences.observe(this@MainActivity, Observer {
                if (it) {
                    startActivity(Intent(this@MainActivity, AuthActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }
            })
            settingsBtn.setOnClickListener {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }
            clearAllPref.setOnClickListener {
                viewModel.logout()
            }
        }
    }

    override fun onResume() {
        viewModel.displayUser()
        viewModel.getSettingsData()
        super.onResume()
    }
}