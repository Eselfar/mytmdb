package com.remiboulier.mytmdb

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.remiboulier.mytmdb.network.models.Result
import kotlinx.android.synthetic.main.item_grid_result.view.*

class MoviesAdapter(private val results: MutableList<Result>) : RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
            MovieViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_grid_result, parent, false))


    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(results.get(position))
    }

    fun update(newResults: List<Result>) {
        results.clear()
        results.addAll(newResults)
        notifyDataSetChanged()
    }
}

class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(result: Result) = with(view) {
        resultTitle.text = result.title
    }
}
