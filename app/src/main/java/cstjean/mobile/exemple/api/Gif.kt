package cstjean.mobile.exemple.api

import android.provider.MediaStore.Images
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class Gif(
    val title: String,
    val id: String,
    val images: Images
)

@JsonClass(generateAdapter = true)
data class Images(
    @Json(name = "fixed_height") val fixedHeight: Image
)
@JsonClass(generateAdapter = true)
data class Image(
    val url: String
)