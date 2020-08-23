package ru.musintimur.lorempicsumexplorer.network


import kotlinx.coroutines.runBlocking
import org.junit.*
import ru.musintimur.lorempicsumexplorer.room.photo.Photo

class RetrofitClientTest {

    private lateinit var client: RetrofitClient
    private lateinit var stubPhoto: Photo

    @Before
    fun setup() {
        client = RetrofitClient()
        stubPhoto = Photo(1,"",1,1,"", "", false)
    }

    @Test
    fun `image url is correct`(){
        val url = RetrofitClient.getSizedImageUrlById(5,640,480)
        Assert.assertEquals("https://picsum.photos/id/5/640/480", url)
    }

    @Test
    fun `photos are loading from network`() {
        val photos = runBlocking {
            client.getPhotos(1, 20)
        }
        Assert.assertEquals(photos.size, 20)
    }

    @Test
    fun `photos from different pages are different`() {
        val firstList = runBlocking {
            client.getPhotos(1, 20)
        }
        val secondList = runBlocking {
            client.getPhotos(2, 20)
        }
        var foundSamePhoto = false
         firstList.forEach scan@ { photo1 ->
            secondList.forEach { photo2 ->
                if (photo1 == photo2) {
                    foundSamePhoto = true
                    return@scan
                }
            }
        }
        Assert.assertEquals(false, foundSamePhoto)
    }

    @Test
    fun `photo from network hs expected name`() {
        val photo = runBlocking {
            client.getPhotoById(1031)
        }
        Assert.assertEquals("Mike Wilson", photo.author)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun `adding photo to network repository is unsupported`(){
        runBlocking {
            client.addPhoto(stubPhoto, null)
        }
        Assert.assertEquals(true, false)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun `checking photo in network repository is unsupported`(){
        val isSaved = runBlocking {
            client.isPhotoSaved(stubPhoto)
        }
        Assert.assertEquals(true, isSaved)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun `deleting photo to network repository is unsupported`(){
        runBlocking {
            client.deletePhoto(stubPhoto)
        }
        Assert.assertEquals(true, false)
    }
}