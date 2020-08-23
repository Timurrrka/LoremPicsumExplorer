package ru.musintimur.lorempicsumexplorer.room.photo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "photos"
)
data class Photo(
    @PrimaryKey
    val id: Int,
    val author: String,
    var width: Int,
    var height: Int,
    val url: String,
    @SerializedName("download_url")
    var downloadUrl: String,
    var isFavorite: Boolean
)