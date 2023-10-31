package com.kristinaefros.challenge.presentation.common.message

import android.content.Context

interface MessageEntity {

    fun messageText(context: Context): String?

}