package com.example.aplikasifilm.layout_configuration.similarMovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasifilm.api.MovieResult

class SimilarMovieAdapter(val data: ArrayList<MovieResult>, val listener: SimilarMovieRecycleViewClickListener): RecyclerView.Adapter<SimilarMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SimilarMovieViewHolder(inflater, parent, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SimilarMovieViewHolder,position: Int) {
        holder.bind(data[position])
    }
}