package ru.musintimur.lorempicsumexplorer.network

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.musintimur.lorempicsumexplorer.model.PhotoRepository
import ru.musintimur.lorempicsumexplorer.room.photo.Photo

class RetrofitClient : PhotoRepository {

    private val picsumApi: PicsumApi

    companion object {
        private const val LOREM_PICSUM_URL = "https://picsum.photos/"
        fun getSizedImageUrlById(photoId: Int, width: Int, height: Int): String {
            return "${LOREM_PICSUM_URL}id/$photoId/$width/$height"
        }
    }

    init {
        val builder = OkHttpClient.Builder()
        val okHttpClient = builder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(LOREM_PICSUM_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
        picsumApi = retrofit.create(PicsumApi::class.java)
    }

    override suspend fun getPhotos(page: Int, limit: Int): List<Photo> {
        return picsumApi.getPhotos(page, limit)
    }

    override suspend fun getPhotoById(id: Int): Photo {
        return picsumApi.getPhotoById(id)
    }

    override suspend fun addPhoto(photo: Photo, context: Context?) {
        throw UnsupportedOperationException("Network addition is not supported.")
    }

    override suspend fun isPhotoSaved(photo: Photo): Boolean {
        throw UnsupportedOperationException("This operation is not supported for network calls.")
    }

    override suspend fun deletePhoto(photo: Photo) {
        throw UnsupportedOperationException("Network deletion is not supported.")
    }
}