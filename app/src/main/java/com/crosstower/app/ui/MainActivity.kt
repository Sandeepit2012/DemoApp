package com.crosstower.app.ui

import com.crosstower.app.R.layout
import com.crosstower.app.R.string
import com.crosstower.app.enums.FetchStatus.FAILED
import com.crosstower.app.enums.FetchStatus.FETCHING
import com.crosstower.app.enums.FetchStatus.NONE
import com.crosstower.app.enums.FetchStatus.SUCCESS
import com.crosstower.app.utils.ImageDownloader.initiateImageDownload
import com.crosstower.app.utils.ImageSharer.initiateImageShare
import com.crosstower.app.viewmodels.PhotoStoryViewModel
import com.crosstower.libraryapi.models.Image
import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val PERM_REQ_CODE = 1451
    }

    lateinit var photoStoryViewModel: PhotoStoryViewModel
    var currentAnimator: ObjectAnimator? = null

    //Initializes all the VMs for the activity to perform operations
    private fun setupViewModel() {
        photoStoryViewModel = ViewModelProviders.of(this).get(PhotoStoryViewModel::class.java)
        photoStoryViewModel.fetchStatus.observe(this, Observer {
            when (it) {
                FETCHING -> contentLoader.show()
                SUCCESS -> contentLoader.hide()
                FAILED -> {
                    contentLoader.hide()
                    AlertDialog.Builder(this)
                        .setTitle(getString(string.err_fetch_story_title))
                        .setMessage(getString(string.err_fetch_story_msg))
                        .setNegativeButton(getString(string.btn_close_app)) { _, _ -> finish() }
                        .setPositiveButton(getString(string.btn_retry)) { _, _ -> refresh() }
                        .setCancelable(false)
                        .show()
                }
            }
        })

        photoStoryViewModel.photoStream.observe(this, Observer {
            goToNextPhoto()
        })
    }

    //Refresh the photos
    private fun refresh() {
        photoStoryViewModel.fetchStatus.value = NONE
        photoStoryViewModel.refreshPhotoStory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        setupViewModel()
        refresh()

    }

    /**
     * A function-calling-function to prevent recursion inside
     */
    private val callGoToNext = { it: Animator -> goToNextPhoto() }

    //Load next photo
    private fun goToNextPhoto() {
        // If we have run out of photos, load next page of API
        if (photoStoryViewModel.photoStream.value?.empty() == true) {
            photoStoryViewModel.refreshPhotoStory(true)
        }
        photoStoryViewModel.photoStream.value?.pop()?.let { image ->
            Glide.with(ivPhotoStory).load(image.link).into(ivPhotoStory)
            tvPhotoTitle.text = image.title

            ivPhotoStory.setOnClickListener {
                image.parentItemId?.let { hash -> AlbumDetailsActivity.start(this, hash) }
            }
            ivPhotoStory.setOnLongClickListener {
                showDetailDialog(image)
                true
            }

            currentAnimator = ObjectAnimator.ofInt(progressPhotoStory, "progress", 0, 100).apply {
                duration = 4000
                interpolator = LinearInterpolator()
                start()
                doOnEnd(callGoToNext)
            }

            // Preload the next photo
            photoStoryViewModel.photoStream.value?.peek()?.let {
                Glide.with(ivPhotoStory).load(it.link).preload()
            }
        }

    }

    //Shows the details to respective image along with buttons
    private fun showDetailDialog(image: Image) {
        currentAnimator?.takeIf { it.isStarted && it.isRunning }?.pause()

        AlertDialog.Builder(this)
            .setTitle(image.title)
            .setMessage(image.description ?: "")
            .setIcon(android.R.drawable.ic_menu_gallery)
            .setOnDismissListener {
                currentAnimator?.takeIf { it.isStarted && it.isPaused }?.resume()

            }
            .setNeutralButton("Download") { _, _ ->
                initiateImageDownload(this, image)
            }
            .setNegativeButton("Share") { _, _ ->
                initiateImageShare(this, image)
            }
            .setPositiveButton("Like") { _, _ -> }
            .show()
    }

    //reallocate memory on resuming
    override fun onResume() {
        super.onResume()
        currentAnimator?.takeIf { it.isStarted && it.isPaused }?.resume()
    }

    //To prevent from memory leak
    override fun onPause() {
        currentAnimator?.takeIf { it.isStarted && it.isRunning }?.pause()
        super.onPause()
    }

}
