package ru.musintimur.lorempicsumexplorer.room.photo

import androidx.room.*

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photos LIMIT :limit OFFSET ((:page - 1) * :limit)")
    suspend fun getPhotos(page: Int, limit: Int): List<Photo>

    @Query("SELECT * FROM photos where id = :id")
    suspend fun getPhotoById(id: Int): Photo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: Photo)

    @Query("SELECT count(*)>0 FROM photos WHERE id=:photoId")
    suspend fun isPhotoSaved(photoId: Int): Boolean

    @Delete
    suspend fun deletePhoto(photo: Photo)

}