package com.example.aplikasifilm.layout_configuration.cast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasifilm.api.model.MovieCastInfo

class CastMovieAdapter(val data: ArrayList<MovieCastInfo>, val listener: CastMovieRecycleViewClickListener): RecyclerView.Adapter<CastMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CastMovieViewHolder(inflater, parent, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CastMovieViewHolder, position: Int) {
        holder.bind(data[position])
    }
}