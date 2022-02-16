package com.crosstower.app.utils

import com.crosstower.libraryapi.models.Image
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

/***
 * Singleton class to share image
 */
object ImageSharer {

    @SuppressLint("SetWorldReadable")
    @JvmStatic
    fun initiateImageShare(activity: Activity, image: Image) {
        val glideFuture = Glide.with(activity).load(image.link).submit()

        GlobalScope.launch(Dispatchers.IO) {
            val drawable = glideFuture.get()
            val bitmap = drawable.toBitmap()
            val shareDir = File(activity.externalCacheDir, "share_images")
            if (!shareDir.exists()) {
                shareDir.mkdirs()
            }

            val tempFile = File(shareDir, "${image.id}.jpeg")
            val fOut = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
            fOut.flush()
            fOut.close()
            tempFile.setReadable(true, false)

            shareImage(activity, tempFile, "image/jpeg")
        }
    }

    //called when share image is clicked, opens options to share with
    @JvmStatic
    fun shareImage(context: Context, file: File, mimeType: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.type = mimeType
        intent.putExtra(
            Intent.EXTRA_STREAM,
            FileProvider.getUriForFile(context, "com.crosstower.app", file)
        )
        context.startActivity(Intent.createChooser(intent, "Share image via"))
    }
}