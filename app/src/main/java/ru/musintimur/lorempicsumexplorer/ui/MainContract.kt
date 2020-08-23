package ru.musintimur.lorempicsumexplorer.ui

interface MainContract {
    fun calculateColumns(): Int
    fun setOnSettingsItemClick(action: () -> Unit)

    enum class ViewVisibility {
        VISIBLE,
        GONE
    }

    fun setupProgressBarVisibility(visibility: ViewVisibility)
}