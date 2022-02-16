package com.crosstower.app.listadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.crosstower.app.R
import com.crosstower.app.listadapters.PhotoListAdapter.PhotoViewHolder
import com.crosstower.libraryapi.models.Image
import kotlinx.android.synthetic.main.list_item_photo.view.*

/***
 * Adapter for Photos
 */
class PhotoListAdapter(
    private val images: MutableList<Image>
) : ListAdapter<Image, PhotoViewHolder>(PhotoDiffCallback()) {

    //used by the observer to update data on view
    fun updateData(newImages: List<Image>) {
        images.clear()
        images.addAll(newImages)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = images.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_photo, parent, false)

        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        //load image to the ui
        holder.itemView.ivPhoto.let {
            Glide.with(it)
                .load(images[position].link)
                .into(it)
        }
    }

    class PhotoViewHolder(itemView: View) : ViewHolder(itemView)

    //handling callback from the view action
    class PhotoDiffCallback : ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean =
            oldItem == newItem

    }
}