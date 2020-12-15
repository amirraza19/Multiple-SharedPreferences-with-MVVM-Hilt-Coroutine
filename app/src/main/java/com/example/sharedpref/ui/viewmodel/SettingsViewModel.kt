package com.example.sharedpref.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedpref.datasource.local.SettingsPreferenceManager
import com.example.sharedpref.model.SettingsModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SettingsViewModel @ViewModelInject constructor(
    private val settingsPreferenceManager: SettingsPreferenceManager
) : ViewModel() {

    var updateSettingsLiveData = MutableLiveData<Boolean>()
    val showSeletedSettings = MutableLiveData<SettingsModel>()

    fun update(showSettings: Boolean, isDark: Boolean) {
        viewModelScope.launch {
            settingsPreferenceManager.updateSettings(showSettings, isDark) {
                updateSettingsLiveData.value = it
            }
        }
    }

    fun showSelected() {
        viewModelScope.launch {
            settingsPreferenceManager.getSettingsDataFlow().collect {
                showSeletedSettings.value = it
            }
        }
    }
}