//package com.kristinaefros.challenge.presentation.common.message
//
//import android.content.Context
//import android.content.Intent
//import ch.urbanconnect.wrapper.R
//import com.kristinaefros.challenge.presentation.common.message.MessageEntity
//import ch.urbanconnect.wrapper.presentation.screens.registration.RegistrationActivity
//import ch.urbanconnect.wrapper.utils.iterator
//import com.kristinaefros.challenge.R
//import okhttp3.ResponseBody
//import org.json.JSONArray
//import org.json.JSONObject
//import retrofit2.HttpException
//import java.net.SocketTimeoutException
//import java.net.UnknownHostException
//import kotlin.coroutines.cancellation.CancellationException
//
//data class ErrorUiModel(
//    private val source: Source,
//    private val error: Throwable?
//) : MessageEntity {
//
//    companion object {
//        fun general(error: Throwable?): ErrorUiModel = ErrorUiModel(Source.GENERAL, error)
//    }
//
//    enum class Source {
//        GENERAL,
//    }
//
//    override fun messageText(context: Context): String? {
//        return when (source) {
//            Source.GENERAL -> general(context)
//        }
//    }
//
//    private fun general(context: Context): String? {
//        return when (error) {
//            null -> {
//                context.getString(R.string.alerts_unknown_error_message)
//            }
//            is CancellationException -> {
//                null
//            }
//            is UnknownHostException, is SocketTimeoutException -> {
//                context.getString(R.string.general_error_no_connectivity)
//            }
//            is HttpException -> {
//                when (error.code()) {
//                    401 -> {
//                        // TODO Will be removed with a proper user storage
//                        val intent = Intent(context, RegistrationActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        context.startActivity(intent)
//                        null
//                    }
//                    else -> {
//                        httpMessage(context, error) ?: context.getString(R.string.alerts_unknown_error_message)
//                    }
//                }
//            }
//            else -> {
//                context.getString(R.string.alerts_unknown_error_message)
//            }
//        }
//    }
//
//    private fun local(context: Context): String {
//        return when (error) {
//            null -> {
//                context.getString(R.string.alerts_unknown_error_message)
//            }
//            else -> {
//                error.message ?: context.getString(R.string.alerts_unknown_error_message)
//            }
//        }
//    }
//
//    private fun httpMessage(context: Context, exception: HttpException): String? = try {
//        val body = exception.response()?.errorBody() ?: exception.response()?.body() as ResponseBody?
//        if (body != null) {
//            val rootObj = JSONObject(body.string())
//            val messages: JSONArray? = rootObj.optJSONArray("messages")
//            messages?.let {
//                val errors = mutableListOf<String>()
//                for (message in messages.iterator<String>()) {
//                    message.let { errors.add(message) }
//                }
//                errors.joinToString(". ")
//            } ?: context.getString(R.string.alerts_unknown_error_message)
//        } else {
//            context.getString(R.string.alerts_unknown_error_message)
//        }
//    } catch (error: Exception) {
//        context.getString(R.string.alerts_unknown_error_message)
//    }
//}