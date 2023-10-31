package com.kristinaefros.challenge.data.preference

import com.kristinaefros.challenge.domain.auth.AuthModel

class AppPreferences {

    private val authStorage = NullablePreference<AuthModel>(KEY_AUTH)

    fun authStorage(): NullablePreference<AuthModel> = authStorage

    companion object {
        private const val KEY_AUTH = "KEY_AUTH"
    }
}