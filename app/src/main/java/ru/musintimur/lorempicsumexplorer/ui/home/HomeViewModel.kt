package ru.musintimur.lorempicsumexplorer.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.launch
import ru.musintimur.lorempicsumexplorer.app.Injection
import ru.musintimur.lorempicsumexplorer.app.MainApp
import ru.musintimur.lorempicsumexplorer.app.preferences.IntProperties
import ru.musintimur.lorempicsumexplorer.room.photo.Photo
import ru.musintimur.lorempicsumexplorer.network.PhotosDataSource
import ru.musintimur.lorempicsumexplorer.ui.ThrowableCallback

class HomeViewModel : ViewModel() {

    private val networkClient = Injection.provideNetworkResource()
    private val repository = Injection.provideRoomRepository()

    private val _photos: LiveData<PagedList<Photo>>
    private val _loading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<Throwable?>()

    init {
        val pageSize = MainApp.preferences.getInt(IntProperties.PAGE_SIZE.propertyName, IntProperties.PAGE_SIZE.defaultValue)
        val initialPageSize = MainApp.preferences.getInt(IntProperties.INITIAL_PAGE_SIZE.propertyName, IntProperties.INITIAL_PAGE_SIZE.defaultValue)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(initialPageSize)
            .setEnablePlaceholders(false)
            .build()

        _loading.postValue(true)
        _photos = getPagedListBuilder(config).build()
    }

    fun getPhotos(): LiveData<PagedList<Photo>> = _photos
    fun getLoading(): LiveData<Boolean> = _loading
    fun getError(): LiveData<Throwable?> = _error

    fun saveToFavorites(photo: Photo, context: Context) = viewModelScope.launch {
        runCatching {
            repository.addPhoto(photo, context)
        }.onSuccess {
            _error.postValue(null)
        }.onFailure {
            _error.postValue(it)
        }
    }

    fun deleteFromFavorites(photo: Photo) = viewModelScope.launch {
        runCatching {
            repository.deletePhoto(photo)
        }.onSuccess {
            _error.postValue(null)
        }.onFailure {
            _error.postValue(it)
        }
    }

    private fun getPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Photo> {

        val dataSourceFactory = object : DataSource.Factory<Int, Photo>() {
            override fun create(): DataSource<Int, Photo> {
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
                return PhotosDataSource(
                    photoWidth,
                    photoHeight,
                    viewModelScope,
                    networkClient,
                    object : ThrowableCallback{
                        override fun onSuccess() {
                            _error.postValue(null)
                            _loading.postValue(false)
                        }

                        override fun onError(e: Throwable) {
                            _error.postValue(e)
                            _loading.postValue(false)
                        }
                    })
            }
        }
        return LivePagedListBuilder(dataSourceFactory, config)
    }

}
