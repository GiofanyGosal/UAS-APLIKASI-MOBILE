package com.example.aplikasifilm.layout_configuration.filmSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasifilm.api.MovieResult

class FilmSearchMovieAdapter(val data: ArrayList<MovieResult>, val listener: FilmSearchRecycleViewClickListener): RecyclerView.Adapter<FilmSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FilmSearchViewHolder(inflater, parent, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FilmSearchViewHolder, position: Int) {
        holder.bind(data[position])
    }
}