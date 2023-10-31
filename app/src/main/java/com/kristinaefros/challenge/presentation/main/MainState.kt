package com.kristinaefros.challenge.presentation.main

import com.kristinaefros.challenge.domain.auth.AuthModel

data class MainState(
    val authModel: AuthModel? = null,
) {
    val authState = if (authModel != null) AuthStateModel.Authorized else AuthStateModel.UnAuthorized
}

sealed class AuthStateModel {
    object UnAuthorized : AuthStateModel()
    object Authorized : AuthStateModel()
}
