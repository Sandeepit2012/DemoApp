package com.crosstower.app.utils

import com.crosstower.app.ui.MainActivity
import com.crosstower.libraryapi.models.Image
import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/***
 * Singleton class for Image Download
 */
object ImageDownloader {
    /**
     * Initiates image download the original sized image as it is.
     */
    @JvmStatic
    fun initiateImageDownload(activity: Activity, image: Image) {
        when (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PackageManager.PERMISSION_GRANTED -> downloadImage(activity, image)
            PackageManager.PERMISSION_DENIED -> {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    MainActivity.PERM_REQ_CODE
                )
            }
        }
    }

    //Download the image from the link
    @JvmStatic
    private fun downloadImage(context: Context, image: Image) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadRequest = DownloadManager.Request(Uri.parse(image.link)).apply {
            val imageExt = image.type?.split("/")?.get(1)

            setMimeType(image.type)
            setTitle(image.title)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            setDescription(image.description ?: "Image from CrossTower App")
            setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "${image.id}.${imageExt}")

        }
        downloadManager.enqueue(downloadRequest)
    }
}