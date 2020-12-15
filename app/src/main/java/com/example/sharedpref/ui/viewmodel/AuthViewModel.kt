package com.example.sharedpref.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedpref.datasource.local.AuthPreferenceManager
import com.example.sharedpref.datasource.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(
    private val authPreferenceManager: AuthPreferenceManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    var loginResponse = MutableLiveData<Boolean>()
    var authTokenInSharedPref = MutableLiveData<String?>()
    var tokenCleared = MutableLiveData<Boolean>()

    fun login(number: Int) {
        viewModelScope.launch {
            val token = authRepository.login()
            authPreferenceManager.saveToken(token)
            authPreferenceManager.saveNumber(number) {
                loginResponse.value = it
            }
        }
    }

    fun getToken() {
        viewModelScope.launch {
            authTokenInSharedPref.value = authPreferenceManager.getToken()
        }
    }

    fun clearAuthPreferences() {
        viewModelScope.launch {
            authPreferenceManager.clear {
                tokenCleared.value = it
            }
        }
    }
}