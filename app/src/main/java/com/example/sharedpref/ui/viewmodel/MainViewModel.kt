package com.example.sharedpref.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.sharedpref.datasource.local.AuthPreferenceManager
import com.example.sharedpref.datasource.local.MainPreferenceManager
import com.example.sharedpref.datasource.local.SettingsPreferenceManager
import com.example.sharedpref.model.SettingsModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val mainPreferenceManager: MainPreferenceManager,
    private val authPreferenceManager: AuthPreferenceManager,
    private val settingsPreferenceManager: SettingsPreferenceManager
) : ViewModel() {

    var userData = MutableLiveData<String?>()
    var clearAllPreferences = MutableLiveData<Boolean>()
    var settingsDataLiveData = MutableLiveData<SettingsModel>()

    fun getSettingsData() {
        viewModelScope.launch {
            settingsPreferenceManager.getSettingsDataFlow().collect {
                settingsDataLiveData.value = it
            }
        }
    }

    fun displayUser() {
        viewModelScope.launch {
            val token: String? = authPreferenceManager.getToken()
            val number: Int? = authPreferenceManager.getNumber()

            userData.value = "Number $number logged in with $token"
        }
    }

    fun logout() {
        viewModelScope.launch {
            mainPreferenceManager.clearAllPref(
                mainPreferenceManager.dataStore,
                authPreferenceManager.dataStore,
                settingsPreferenceManager.dataStore
            ) {
                clearAllPreferences.value = it
            }
        }
    }
}