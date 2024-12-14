package com.example.aplikasifilm.layout_configuration.recommendedMovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasifilm.api.MovieResult

class RecommendedMovieAdapter(val data: ArrayList<MovieResult>, val listener: RecommendedMovieRecycleViewClickListener): RecyclerView.Adapter<RecommendedMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecommendedMovieViewHolder(inflater, parent, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecommendedMovieViewHolder,position: Int) {
        holder.bind(data[position])
    }
}