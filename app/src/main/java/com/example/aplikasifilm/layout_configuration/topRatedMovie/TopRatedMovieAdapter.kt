package com.example.aplikasifilm.layout_configuration.topRatedMovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasifilm.api.TopRatedMovieResult

class TopRatedMovieAdapter(val data: ArrayList<TopRatedMovieResult>, val listener: TopRatedMovieRecycleViewClickListener): RecyclerView.Adapter<TopRatedMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TopRatedMovieViewHolder(inflater, parent, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TopRatedMovieViewHolder, position: Int) {
        holder.bind(data[position])
    }
}