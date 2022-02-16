package com.crosstower.libraryapi.interfaces

import com.crosstower.libraryapi.responses.AlbumCommentsResponse
import com.crosstower.libraryapi.responses.AlbumResponse
import com.crosstower.libraryapi.responses.GalleryResponse
import retrofit2.http.GET
import retrofit2.http.Path

/***
 * All apis along with methods for retrofit calls
 */
interface OSApi {

    @GET("gallery/t/{tag}/{sort}/{page}")
    suspend fun getGalleryByTag(
        @Path("tag") tag: String,
        @Path("sort") sortBy: String = "time",
        @Path("page") pageNum: Int = 1
    ): GalleryResponse

    @GET("album/{albumHash}")
    suspend fun getAlbum(
        @Path("albumHash") albumHash: String
    ): AlbumResponse

    @GET("album/{albumHash}/comments")
    suspend fun getAlbumComments(
        @Path("albumHash") albumHash: String
    ): AlbumCommentsResponse
}