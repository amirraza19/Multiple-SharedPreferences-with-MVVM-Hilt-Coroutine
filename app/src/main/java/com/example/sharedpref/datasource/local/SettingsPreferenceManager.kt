package com.example.sharedpref.datasource.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.example.sharedpref.model.SettingsModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsPreferenceManager @Inject constructor(@ApplicationContext context: Context) :
    BasePreferenceManager() {

    val dataStore = context.createDataStore(name = "SettingsPref")

    suspend fun updateSettings(
        showSettings: Boolean,
        isDark: Boolean,
        block: ((Boolean) -> Unit) = {}
    ) {
        save(Keys.SHOW_SETTINGS.name, showSettings, dataStore)
        save(Keys.IS_DARK.name, isDark, dataStore) {
            block(it)
        }
    }

    suspend fun getSettingsDataFlow() =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.e("TAG", "getValueFlow: $exception")
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                val showSettings = preference[Keys.SHOW_SETTINGS] ?: false
                val isDark = preference[Keys.IS_DARK] ?: false
                SettingsModel(showSettings, isDark)
            }

    override suspend fun clear(block: ((Boolean) -> Unit)?) {

    }

    object Keys {
        val SHOW_SETTINGS = preferencesKey<Boolean>("SHOW_SETTINGS")
        val IS_DARK = preferencesKey<Boolean>("IS_DARK")
    }
}