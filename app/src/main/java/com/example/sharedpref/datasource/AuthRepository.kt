package com.example.sharedpref.datasource

import kotlinx.coroutines.delay
import javax.inject.Inject

class AuthRepository @Inject constructor(/*private val webservice: Webservice*/) {

    suspend fun login(): String {
        delay(500) //Fake network call and wait for the result, meanwhile loader can be shown
        return "R4a#1S-=3a3Dd2$2sc%FXS@dfr=="
    }
}