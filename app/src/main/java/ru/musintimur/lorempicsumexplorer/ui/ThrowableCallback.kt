package ru.musintimur.lorempicsumexplorer.ui

interface ThrowableCallback {
    fun onSuccess()
    fun onError(e: Throwable)
}