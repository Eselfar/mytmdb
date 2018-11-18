package com.remiboulier.mytmdb

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.remiboulier.mytmdb.network.models.NPMovie
import com.remiboulier.mytmdb.repository.NetworkState
import kotlinx.android.synthetic.main.item_grid_result.view.*

class MoviesAdapter(
        private val retryCallback: () -> Unit)
    : PagedListAdapter<NPMovie, MovieViewHolder>(DIFF_CALLBACK) {

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
            MovieViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_grid_result, parent, false))

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object :
                DiffUtil.ItemCallback<NPMovie>() {
            // The ID property identifies when items are the same.
            override fun areItemsTheSame(oldItem: NPMovie, newItem: NPMovie) =
                    oldItem.id == newItem.id

            // Use the "==" operator to know when an item's content changes.
            // Implement equals(), or write custom data comparison logic here.
            override fun areContentsTheSame(
                    oldItem: NPMovie, newItem: NPMovie) = oldItem == newItem
        }
    }
}

class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(npMovie: NPMovie?) = with(view) {
        if (npMovie != null)
            resultTitle.text = npMovie.title
    }
}
