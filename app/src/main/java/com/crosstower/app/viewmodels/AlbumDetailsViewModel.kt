package com.crosstower.app.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crosstower.app.enums.FetchStatus
import com.crosstower.app.enums.FetchStatus.*
import com.crosstower.libraryapi.OSApi
import com.crosstower.libraryapi.models.Comment
import com.crosstower.libraryapi.models.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/***
 * VM for album details
 */
class AlbumDetailsViewModel : ViewModel() {
    //Live data for Imagelist
    val images = MutableLiveData<List<Image>>()

    //Livedata for comment list
    val comments = MutableLiveData<List<Comment>>()

    //Livedata for api state
    val fetchStatus = MutableLiveData<FetchStatus>(NONE)

    /***
     * All the logical operation to post livedata wrt api call for comments
     */
    fun loadAlbum(albumHash: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                fetchStatus.postValue(FETCHING)
                val album = OSApi.api.getAlbum(albumHash)
                val albumComments = OSApi.api.getAlbumComments(albumHash)

                album.data?.images?.let { images.postValue(it) }
                albumComments.data
                    ?.filter { !(it.deleted ?: false) }
                    ?.let { comments.postValue(it) } //postvalue will enable the activity to observe the state and load comments view

                fetchStatus.postValue(SUCCESS)
            } catch (e: Exception) {
                Log.e("Album", "Error fetching", e)
                fetchStatus.postValue(FAILED)
            }

        }
    }
}