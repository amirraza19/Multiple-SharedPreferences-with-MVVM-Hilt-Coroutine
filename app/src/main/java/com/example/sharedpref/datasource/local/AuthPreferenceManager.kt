package com.example.sharedpref.datasource.local

import android.content.Context
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthPreferenceManager @Inject constructor(@ApplicationContext context: Context) :
    BasePreferenceManager() {

    val dataStore = context.createDataStore(name = "AuthPref")

    suspend fun saveNumber(number: Int, block: ((Boolean) -> Unit)) {
        super.save(Keys.NUMBER, number, dataStore, block)
    }

    suspend fun getNumber() = getValue<Int>(Keys.NUMBER, dataStore)

    suspend fun saveToken(token: String) {
        save(Keys.AUTH_TOKEN, token, dataStore)
    }

    suspend fun getToken() = getValue<String>(Keys.AUTH_TOKEN, dataStore)

    override suspend fun clear(block: ((Boolean) -> Unit)?) {
        try {
            dataStore.edit { it.clear() }
            block?.let {
                withContext(Dispatchers.Main) { it(true) }
            }
        } catch (e: Exception) {
            block?.let {
                withContext(Dispatchers.Main) { it(false) }
            }
        }
    }

    object Keys {
        const val AUTH_TOKEN = "AUTH_TOKEN"
        const val NUMBER = "NUMBER"
    }
}