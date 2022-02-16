package com.crosstower.app.viewmodels

import com.crosstower.app.enums.FetchStatus
import com.crosstower.app.enums.FetchStatus.FAILED
import com.crosstower.app.enums.FetchStatus.FETCHING
import com.crosstower.app.enums.FetchStatus.NONE
import com.crosstower.app.enums.FetchStatus.SUCCESS
import com.crosstower.libraryapi.OSApi
import com.crosstower.libraryapi.models.Image
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class PhotoStoryViewModel : ViewModel() {
    private var page = 0
    //Livedata for images
    val photoStream = MutableLiveData<Stack<Image>>()
    //Livedata for Api state
    val fetchStatus = MutableLiveData<FetchStatus>(NONE)

    /***
     * All the logical operation to post livedata wrt api call for images
     */
    fun refreshPhotoStory(nextPage: Boolean = false) {
        if (nextPage) { page++ } else { page = 1 }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                fetchStatus.postValue(FETCHING)
                val gallery = OSApi.api.getGalleryByTag("science_and_tech", pageNum = page)
                val images = gallery.data?.items
                    ?.flatMap { p ->
                        p.images?.map {
                            it.apply {
                                title = title ?: p.title
                                parentItemId = p.id
                            }
                        } ?: listOf()
                    }
                    ?.filter { i -> i.type?.startsWith("image/") ?: false }
                fetchStatus.postValue(SUCCESS)
                //postvalue will enable the activity to observe the state and load images to view
                photoStream.postValue(
                    Stack<Image>().apply { addAll(images!!.reversed()) }
                )
            } catch (e: Exception) {
                Log.e("PhotoStory", "Error fetching", e)
                fetchStatus.postValue(FAILED)
            }

        }

    }
}