package ru.musintimur.lorempicsumexplorer.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.musintimur.lorempicsumexplorer.app.MainApp
import ru.musintimur.lorempicsumexplorer.app.preferences.IntProperties

class SettingsViewModel : ViewModel() {

    private val _settings = MutableLiveData<Map<String, Int>>()
    private val _isSaved = MutableLiveData(false)

    fun getSettings(): LiveData<Map<String, Int>> = _settings
    fun isSaved(): LiveData<Boolean> = _isSaved

    fun loadSettings() {
        val settings = mutableMapOf<String, Int>()
        IntProperties.values().forEach {
            settings[it.propertyName] = MainApp.preferences.getInt(it.propertyName, it.defaultValue)
        }
        _settings.postValue(settings)
    }

    fun saveSettings(settings: Map<String, Int>) {
        MainApp.preferences.edit().run {
            settings.forEach { entry ->
                putInt(entry.key, entry.value)
            }
            apply()
        }
        _isSaved.postValue(true)
    }

}