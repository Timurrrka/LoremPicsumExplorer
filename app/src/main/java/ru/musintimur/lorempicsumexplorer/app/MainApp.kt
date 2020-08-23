package ru.musintimur.lorempicsumexplorer.app

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import ru.musintimur.lorempicsumexplorer.app.preferences.IntProperties
import ru.musintimur.lorempicsumexplorer.app.preferences.Preferences
import ru.musintimur.lorempicsumexplorer.room.PhotoDatabase

open class MainApp : Application() {

    companion object {

        lateinit var database: PhotoDatabase
        lateinit var preferences: SharedPreferences

        private lateinit var instance: MainApp

    }

    override fun onCreate() {
        instance = this
        super.onCreate()

        database = Room.databaseBuilder(this, PhotoDatabase::class.java, "photo_database").build()
        preferences = getSharedPreferences(Preferences.PREFERENCES.filename, Activity.MODE_PRIVATE)
        checkDefaultPreferences()
    }

    private fun checkDefaultPreferences() {
        IntProperties.values().forEach {
            if (!preferences.contains(it.propertyName)) {
                preferences.edit()
                    .putInt(
                        it.propertyName,
                        it.defaultValue
                    ).apply()
            }
        }
    }
}