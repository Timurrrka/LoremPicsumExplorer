package ru.musintimur.lorempicsumexplorer.app.preferences

enum class IntProperties(
    val preferencesFile: Preferences,
    val propertyName: String,
    val defaultValue: Int
) {
    PHOTO_WIDTH(Preferences.PREFERENCES, "preferablePhotoWidth", 600),
    PHOTO_HEIGHT(Preferences.PREFERENCES, "preferablePhotoHeight", 600),
    PAGE_SIZE(Preferences.PREFERENCES, "loadingPageSize", 20),
    INITIAL_PAGE_SIZE(Preferences.PREFERENCES, "loadingInitialPageSize", PAGE_SIZE.defaultValue * 2)
}