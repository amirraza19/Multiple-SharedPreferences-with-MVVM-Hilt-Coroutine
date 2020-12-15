package com.example.sharedpref.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainPreferenceManager @Inject constructor(@ApplicationContext context: Context) :
    BasePreferenceManager() {

    val dataStore = context.createDataStore(name = "MainPref")

    override suspend fun clear(block: ((Boolean) -> Unit)?) {

    }

    suspend inline fun <reified T : Any> save(key: String, value: T, crossinline block: ((Boolean) -> Unit) = {}) {
        super.save(key, value, dataStore) {
            block(it)
        }
    }

    suspend inline fun <reified T : Any> getValue(key: String): T? {
        return super.getValue(key, dataStore)
    }

    suspend fun clearAllPref(vararg dataStore: DataStore<Preferences>, block: (Boolean) -> Unit) {
        super.clearAll(dataStore) {
            block(it)
        }
    }
}