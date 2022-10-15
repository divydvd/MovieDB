package com.divy.moviedb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.divy.moviedb.databinding.MovieListItemBinding

class MovieListAdapter(val dataList: List<MovieListResult>, val movieItemClickListener: MovieItemClickListener): RecyclerView.Adapter<MovieListAdapter.MoviesViewHolder>() {
    inner class MoviesViewHolder(val binding: MovieListItemBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.binding.tvMovieDesc.text = dataList[position].overview
        holder.binding.tvMovieTitle.text = dataList[position].original_title
        Glide.with(holder.itemView.context)
            .load(BuildConfig.MOVIE_DB_IMAGE_BASE_URL + dataList[position].backdrop_path)
            .into(holder.binding.ivMovieThumbnail)
        holder.binding.clWrapper.setOnClickListener {
            movieItemClickListener.onMovieItemClicked(dataList[position])
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    interface MovieItemClickListener {
        fun onMovieItemClicked(result: MovieListResult)
    }

}