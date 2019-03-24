package com.developerdru.flickflyers.utils

import com.developerdru.flickflyers.data.entities.Photo
import com.google.gson.Gson

fun Photo.generateImageURL() = "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg"

fun Photo.toJSON() = Gson().toJson(this)

fun Photo.fromJSON(jsonRep: String) = Gson().fromJson(jsonRep, this.javaClass)
