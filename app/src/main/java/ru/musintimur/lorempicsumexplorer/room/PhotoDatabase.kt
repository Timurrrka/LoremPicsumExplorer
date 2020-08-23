package ru.musintimur.lorempicsumexplorer.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.musintimur.lorempicsumexplorer.room.photo.Photo
import ru.musintimur.lorempicsumexplorer.room.photo.PhotoDao

@Database(entities = [(Photo::class)], version = 1)
abstract class PhotoDatabase : RoomDatabase() {

        abstract fun photoDao(): PhotoDao

}