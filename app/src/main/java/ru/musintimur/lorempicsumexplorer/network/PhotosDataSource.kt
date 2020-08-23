package ru.musintimur.lorempicsumexplorer.network

import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.musintimur.lorempicsumexplorer.app.Injection
import ru.musintimur.lorempicsumexplorer.model.PhotoRepository
import ru.musintimur.lorempicsumexplorer.room.RoomRepository
import ru.musintimur.lorempicsumexplorer.room.photo.Photo
import ru.musintimur.lorempicsumexplorer.ui.ThrowableCallback

class PhotosDataSource(
    private val preferableWidth: Int,
    private val preferableHeight: Int,
    private val scope: CoroutineScope,
    private val source: PhotoRepository,
    private val viewModelCallback: ThrowableCallback
) : PageKeyedDataSource<Int, Photo>() {

    companion object {
        const val INITIAL_PAGE = 1
    }

    private val db = if (source is RoomRepository) source else Injection.provideRoomRepository()
    private var page = INITIAL_PAGE

    private fun nextPage(): Int {
        page = page.inc()
        return page
    }

    private fun prevPage(): Int {
        page = page.dec()
        return page
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Photo>) {
        scope.launch {
            runCatching {
                loadPhotos(page, params.requestedLoadSize)
            }.onSuccess { photos ->
                callback.onResult(photos, null, nextPage())
                viewModelCallback.onSuccess()
            }.onFailure {
                viewModelCallback.onError(it)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        scope.launch {
            runCatching {
                loadPhotos(page, params.requestedLoadSize)
            }.onSuccess { photos ->
                callback.onResult(photos, prevPage())
                viewModelCallback.onSuccess()
            }.onFailure {
                viewModelCallback.onError(it)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        scope.launch {
            runCatching {
                loadPhotos(page, params.requestedLoadSize)
            }.onSuccess { photos ->
                callback.onResult(photos, nextPage())
                viewModelCallback.onSuccess()
            }.onFailure {
                viewModelCallback.onError(it)
            }
        }
    }

    private suspend fun loadPhotos(currentPage: Int, limit: Int): List<Photo> = withContext(Dispatchers.IO) {
        source.getPhotos(currentPage, limit).map { photo ->
            if (source is RetrofitClient) {
                photo.apply {
                    width = preferableWidth
                    height = preferableHeight
                    downloadUrl = RetrofitClient.getSizedImageUrlById(photo.id, preferableWidth, preferableHeight)
                    isFavorite = db.isPhotoSaved(photo)

                }
            } else photo
        }
    }
}