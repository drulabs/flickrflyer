package com.developerdru.flickflyers.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "flickr_pics")
data class Photo(
    @PrimaryKey @ColumnInfo(name = "photo_id") val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    var tag: String
)

data class PhotoResponse(
    val page: Int,
    val pages: Int,
    val itemsPerPage: Int,
    val total: Int,
    @SerializedName("photo")
    val photos: List<Photo>
)

data class PhotoResponseWrapper(
    @SerializedName("photos") val photoResponse: PhotoResponse,
    @SerializedName("stat") val status: String
)