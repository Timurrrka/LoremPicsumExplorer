package ru.musintimur.lorempicsumexplorer.model.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.musintimur.lorempicsumexplorer.room.PhotoDatabase
import ru.musintimur.lorempicsumexplorer.room.photo.Photo
import ru.musintimur.lorempicsumexplorer.room.photo.PhotoDao
import java.io.IOException


class RoomRepositoryTest {

    private lateinit var photoDao: PhotoDao
    private lateinit var db: PhotoDatabase
    private lateinit var stubPhoto1: Photo
    private lateinit var stubPhoto2: Photo

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, PhotoDatabase::class.java).build()
        photoDao = db.photoDao()
        stubPhoto1 = Photo(1,"",1,1,"", "", false)
        stubPhoto2 = Photo(2,"",1,1,"", "", false)
        runBlocking {
            photoDao.insertPhoto(stubPhoto1)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun firstPhotoIsSaved() {
        val isSaved = runBlocking {
            photoDao.isPhotoSaved(stubPhoto1.id)
        }
        Assert.assertEquals(true, isSaved)
    }

    @Test
    fun secondPhotoIsNotSaved() {
        val isSaved = runBlocking {
            photoDao.isPhotoSaved(stubPhoto2.id)
        }
        Assert.assertEquals(false, isSaved)
    }

    @Test
    fun testQueryListOfPhotos() {
        var photos: List<Photo> = runBlocking {
            photoDao.getPhotos(1, 20)
        }
        Assert.assertEquals(1, photos.size)
        runBlocking {
            photoDao.insertPhoto(stubPhoto2)
        }
        photos = runBlocking {
            photoDao.getPhotos(1, 20)
        }
        Assert.assertEquals(2, photos.size)
    }

    @Test
    fun testQueryPhotosById() {
        val photo = runBlocking {
            photoDao.getPhotoById(stubPhoto1.id)
        }
        Assert.assertEquals(stubPhoto1, photo)
    }

    @Test
    fun testDeletionFromDb() {
        runBlocking {
            photoDao.deletePhoto(stubPhoto1)
        }
        val isSaved = runBlocking {
            photoDao.isPhotoSaved(stubPhoto1.id)
        }
        Assert.assertEquals(false, isSaved)
    }
}