package com.example.sharedpref.datasource.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException

abstract class BasePreferenceManager {
    abstract suspend fun clear(block: ((Boolean) -> Unit)? = null)

    /**
     * Method to clear all Datastore preferences(Key-Values pairs) provided
     *
     * @param dataStore         array of dataStores
     * @param block             callback function - Optional
     */
    suspend fun clearAll(dataStore: Array<out DataStore<Preferences>>, block: ((Boolean) -> Unit)? = null) {
        try {
            for (data in dataStore) {
                data.edit { it.clear() }
            }
            block?.let {
                withContext(Dispatchers.Main) { it(true) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            block?.let {
                withContext(Dispatchers.Main) { it(false) }
            }
        }
    }

    /**
     * Generic method to save key-value pairs in preferences
     *
     * @param   key         Key to store in preference
     * @param   value       Generic Typed value to store against key
     * @param   dataStore   DataStore preference in which the key-value to be stored
     * @param   block       Callback to receive the success/fail storage operation
     */
    suspend inline fun <reified T : Any> save(key: String, value: T, dataStore: DataStore<Preferences>,
        noinline block: ((Boolean) -> Unit)? = null) {
        try {
            val dataStoreKey = preferencesKey<T>(key)
            dataStore.edit { preference ->
                preference[dataStoreKey] = value
                block?.let {
                    withContext(Dispatchers.Main) { it(true) }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            block?.let {
                withContext(Dispatchers.Main) { it(false) }
            }
        }
    }

    /**
     * Generic method to get values from DataStore preference
     *
     * @param   key         Key provide to retrieve value
     * @param   datastore   DataStore preference from which the value to be retrieve
     *
     * @return  T?          Generic typed value or null if not found
     */
    suspend inline fun <reified T : Any> getValue(key: String, datastore: DataStore<Preferences>): T? {
        return try {
            val dataStoreKey = preferencesKey<T>(key)
            val preferences = datastore.data.first()
            preferences[dataStoreKey] as T
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Generic method to get values from DataStore preference in Flow typed
     *
     * @param   key         Key provide to retrieve value
     * @param   datastore   DataStore preference from which the value to be retrieve
     *
     * @return  Flow<T>     Generic typed value as flow
     */
    suspend fun <T> getValueAsFlow(key: String, datastore: DataStore<Preferences>): Flow<T> =
        datastore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.e("TAG", "getValueFlow: $exception")
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                val dataStoreKey = preferencesKey<String>(key)
                preference[dataStoreKey] as T
            }
}