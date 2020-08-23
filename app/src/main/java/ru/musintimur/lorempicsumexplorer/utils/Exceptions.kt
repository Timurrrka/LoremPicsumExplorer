package ru.musintimur.lorempicsumexplorer.utils

import java.io.IOException
import java.net.ConnectException
import java.net.MalformedURLException

fun checkExceptionType(e: Throwable): String {
    return when(e) {
        is MalformedURLException -> {
            "MalformedURLException:\n\n${e.message}\n\nInvalid URL?"
        }
        is ConnectException -> {
            "ConnectException: \n\n${e.message}\n\nCheck internet connection."
        }
        is IOException -> {
            "IOException reading data:\n\n${e.message}"
        }
        is SecurityException -> {
            "Security exception:\n\n${e.message}\n\nNeeds permission?"
        }
        else -> {
            "Unknown exception:\n\n${e.message}"
        }
    }
}