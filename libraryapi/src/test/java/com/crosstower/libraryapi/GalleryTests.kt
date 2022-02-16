package com.crosstower.libraryapi

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

public class GalleryTests {

    @Test
    fun getSciTechGallery() {
        runBlocking {
            val gallery = OSApi.api.getGalleryByTag("science_and_tech")
            assertEquals(gallery.status, 200)
            assertTrue(gallery.success)
        }
    }
}
