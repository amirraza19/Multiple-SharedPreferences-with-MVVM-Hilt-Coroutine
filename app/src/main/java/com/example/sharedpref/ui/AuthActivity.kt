package com.example.sharedpref.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.sharedpref.R
import com.example.sharedpref.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        lifecycleScope.launch {
            viewModel.loginResponse.observe(this@AuthActivity, Observer {
                if (it) {
                    startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                    finish()
                } else {
                    Log.e("TAG", "onCreate: login error")
                }
            })

            viewModel.authTokenInSharedPref.observe(this@AuthActivity, Observer {
                clearTokenBtn.visibility = if (it == null) View.GONE else View.VISIBLE
                showTokenTV.text = "Token: $it"
            })

            viewModel.tokenCleared.observe(this@AuthActivity, Observer {
                clearTokenBtn.visibility = if (it) View.GONE else View.VISIBLE
                showTokenTV.text = if (it) "Token removed: $it" else "Error in clearing data"
            })

            loginBtn.setOnClickListener {
                val number =
                    if (numberET.text.isNullOrEmpty()) 1234 else numberET.text.toString().toInt()
                viewModel.login(number)
            }

            clearTokenBtn.setOnClickListener {
                viewModel.clearAuthPreferences()
            }
        }
    }

    override fun onResume() {
        viewModel.getToken()
        super.onResume()
    }
}