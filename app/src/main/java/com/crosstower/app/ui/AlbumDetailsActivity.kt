package com.crosstower.app.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.crosstower.app.CrossTowerApp
import com.crosstower.app.R.layout
import com.crosstower.app.di.components.DaggerActivityComponent
import com.crosstower.app.enums.FetchStatus.*
import com.crosstower.app.listadapters.CommentListAdapter
import com.crosstower.app.listadapters.PhotoListAdapter
import com.crosstower.app.viewmodels.AlbumDetailsViewModel
import com.crosstower.libraryapi.interfaces.OSApi
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import kotlinx.android.synthetic.main.activity_album_details.*
import javax.inject.Inject

class AlbumDetailsActivity : AppCompatActivity() {

    val daggerActivityComponent = DaggerActivityComponent.builder()
        .appComponent((application as CrossTowerApp).appComponent)
        .build()

    @Inject
    lateinit var imgApi: OSApi

    companion object {
        const val BUNDLE_KEY_ALBUM_HASH = "album_hash"

        @JvmStatic
        fun start(activity: Activity, albumHash: String) {
            activity.startActivity(
                Intent(activity, AlbumDetailsActivity::class.java).apply {
                    putExtra(BUNDLE_KEY_ALBUM_HASH, albumHash)
                })
        }
    }

    lateinit var albumHash: String
    lateinit var albumDetailsViewModel: AlbumDetailsViewModel

    val photoListAdapter = PhotoListAdapter(arrayListOf())
    val commentListAdapter = CommentListAdapter(arrayListOf())

    //Initialize all the required views in activity
    private fun initViews() {
        vpPhotos.adapter = photoListAdapter
        TabLayoutMediator(tlIndicator, vpPhotos,
            TabConfigurationStrategy { tab, position ->
                tab.text = (position + 1).toString()
            }).attach()

        rvComments.layoutManager = LinearLayoutManager(this)
        rvComments.adapter = commentListAdapter
    }

    //Initializes all the VMs for the activity to perform operations
    private fun initViewModel() {
        albumDetailsViewModel = ViewModelProviders.of(this).get(AlbumDetailsViewModel::class.java)
        albumDetailsViewModel.fetchStatus.observe(this, Observer {
            when (it) {
                FETCHING -> contentLoader.show()
                SUCCESS -> contentLoader.hide()
                FAILED -> {
                    contentLoader.hide()
                    Toast.makeText(this, "Could not fetch this album", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        })
        albumDetailsViewModel.loadAlbum(albumHash)
        albumDetailsViewModel.images.observe(this, Observer {
            photoListAdapter.updateData(it.toMutableList())

        })
        albumDetailsViewModel.comments.observe(this, Observer {
            commentListAdapter.updateData(it.toMutableList())
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        daggerActivityComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_album_details)

        albumHash = intent.getStringExtra(BUNDLE_KEY_ALBUM_HASH) ?: ""
        if (albumHash.isEmpty()) {
            Log.e("Album", "Started with empty Album Hash")
            finish()
        }

        initViews()
        initViewModel()
    }
}
