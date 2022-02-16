package com.crosstower.libraryapi

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

public class AlbumTests {

    @Test
    fun getAlbum() {
        runBlocking {
            val album = OSApi.api.getAlbum("OICg9SZ")
            assertEquals(album.status, 200)
            assertTrue(album.success)
        }
    }

    @Test
    fun getAlbumComments() {
        runBlocking {
            val comments = OSApi.api.getAlbumComments("OICg9SZ")
            assertEquals(comments.status, 200)
            assertTrue(comments.success)
            assertTrue(comments.data!!.isNotEmpty())
        }
    }
}
