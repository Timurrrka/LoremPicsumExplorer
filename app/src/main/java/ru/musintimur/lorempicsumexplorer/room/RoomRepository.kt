package ru.musintimur.lorempicsumexplorer.room

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.musintimur.lorempicsumexplorer.app.MainApp
import ru.musintimur.lorempicsumexplorer.app.preferences.IntProperties
import ru.musintimur.lorempicsumexplorer.model.PhotoRepository
import ru.musintimur.lorempicsumexplorer.room.photo.Photo
import ru.musintimur.lorempicsumexplorer.room.photo.PhotoDao
import ru.musintimur.lorempicsumexplorer.network.RetrofitClient
import ru.musintimur.lorempicsumexplorer.utils.saveBitmapOnDisc

@Suppress("BlockingMethodInNonBlockingContext")
class RoomRepository : PhotoRepository {

    private val photoDao: PhotoDao = MainApp.database.photoDao()

    override suspend fun getPhotos(page: Int, limit: Int): List<Photo> = withContext(Dispatchers.IO) {
        photoDao.getPhotos(page, limit)
    }

    override suspend fun getPhotoById(id: Int): Photo = withContext(Dispatchers.IO) {
        photoDao.getPhotoById(id)
    }

    override suspend fun addPhoto(photo: Photo, context: Context?) = withContext(Dispatchers.IO) {
        val photoWidth = MainApp.preferences
            .getInt(
                IntProperties.PHOTO_WIDTH.propertyName,
                IntProperties.PHOTO_WIDTH.defaultValue
            )
        val photoHeight = MainApp.preferences
            .getInt(
                IntProperties.PHOTO_HEIGHT.propertyName,
                IntProperties.PHOTO_HEIGHT.defaultValue
            )
        var localUrl = Uri.parse(photo.downloadUrl)
        context?.let {
            val networkUrl = RetrofitClient.getSizedImageUrlById(photo.id, photoWidth, photoHeight)
            val bitmap: Bitmap = Picasso.get().load(networkUrl).get()
            localUrl = saveBitmapOnDisc(bitmap, context, photo.id.toString())
        }

        photoDao.insertPhoto(photo.apply {
            downloadUrl = localUrl.toString()
            isFavorite = true
        })
    }

    override suspend fun isPhotoSaved(photo: Photo): Boolean = withContext(Dispatchers.IO) {
        photoDao.isPhotoSaved(photo.id)
    }

    override suspend fun deletePhoto(photo: Photo) = withContext(Dispatchers.IO) {
        photoDao.deletePhoto(photo)
    }
}