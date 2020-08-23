package ru.musintimur.lorempicsumexplorer.model

import android.content.Context
import ru.musintimur.lorempicsumexplorer.room.photo.Photo

interface PhotoRepository {

    suspend fun getPhotos(page: Int, limit: Int): List<Photo>

    suspend fun getPhotoById(id: Int): Photo

    suspend fun addPhoto(photo: Photo, context: Context?)

    suspend fun isPhotoSaved(photo: Photo): Boolean

    suspend fun deletePhoto(photo: Photo)

}