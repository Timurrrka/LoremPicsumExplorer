package ru.musintimur.lorempicsumexplorer.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.musintimur.lorempicsumexplorer.room.photo.Photo

interface PicsumApi {

    @GET("v2/list")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<Photo>

    @GET("/id/{id}/info")
    suspend fun getPhotoById(
        @Path("id") id: Int
    ): Photo

}