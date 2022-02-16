package com.crosstower.app.listadapters

import com.crosstower.app.R
import com.crosstower.app.listadapters.CommentListAdapter.CommentViewHolder
import com.crosstower.libraryapi.models.Comment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.android.synthetic.main.list_item_comment.view.*
/***
 * Comments adapter for each photo
 */
class CommentListAdapter(
    private val comments: MutableList<Comment>
) : ListAdapter<Comment, CommentViewHolder>(CommentDiffCallback()) {

    //Used to update data by the observer
    fun updateData(newComments: List<Comment>) {
        comments.clear()
        comments.addAll(newComments)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = comments.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_comment, parent, false)

        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        //set values to ui items
        holder.itemView.tvUsername.text = comments[position].author
        holder.itemView.tvComment.text = comments[position].comment
        holder.itemView.tvPts.text = String.format(
            holder.itemView.resources.getString(R.string.comment_pts),
            comments[position].points ?: 0
        )
    }

    class CommentViewHolder(itemView: View) : ViewHolder(itemView) {}

    //Item callback handling for actions
    class CommentDiffCallback : ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
            oldItem == newItem

    }
}