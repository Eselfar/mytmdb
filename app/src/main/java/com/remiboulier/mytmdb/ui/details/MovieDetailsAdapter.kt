package com.remiboulier.mytmdb.ui.details

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.remiboulier.mytmdb.R
import com.remiboulier.mytmdb.extension.getPosterUrl
import com.remiboulier.mytmdb.network.models.Part
import com.remiboulier.mytmdb.util.GlideRequests

/**
 * Created by Remi BOULIER on 18/11/2018.
 * email: boulier.r.job@gmail.com
 */

class MovieDetailsAdapter(private val parts: MutableList<Part>,
                          private val glide: GlideRequests,
                          private val onClick: (movieId: Int) -> Unit) : RecyclerView.Adapter<MovieDetailsAdapter.PartViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recycler_movie_details_part, parent, false) as ImageView

        return PartViewHolder(view, onClick)
    }

    override fun getItemCount() = parts.size

    override fun onBindViewHolder(holder: PartViewHolder, position: Int) {
        holder.bind(parts[position], glide)
    }

    fun update(newParts: List<Part>?) {
        parts.clear()
        if (newParts != null)
            parts.addAll(newParts)
        notifyDataSetChanged()
    }


    inner class PartViewHolder(val view: ImageView, onClick: (movieId: Int) -> Unit) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener { v -> onClick(parts[adapterPosition].id!!) }
        }

        fun bind(part: Part, glide: GlideRequests) {
            val url = part.posterPath?.getPosterUrl()
            if (url != null) {
                glide.load(url)
                        .placeholder(R.drawable.img_poster_empty)
                        .into(view)
            } else {
                view.setImageResource(R.drawable.img_poster_empty)
            }
        }
    }
}