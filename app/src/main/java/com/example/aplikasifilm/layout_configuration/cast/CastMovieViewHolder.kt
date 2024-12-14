package com.example.aplikasifilm.layout_configuration.cast

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.aplikasifilm.R
import com.example.aplikasifilm.api.model.MovieCastInfo

class CastMovieViewHolder(inflater: LayoutInflater, parent: ViewGroup, val listener: CastMovieRecycleViewClickListener):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.cast_item_template, parent, false)), OnClickListener{

    private var castImageView: ImageView? = null
    private var castName: TextView? = null
    private var castCharacterName: TextView? = null

    init {
        castImageView = itemView.findViewById(R.id.film_detail_cast_image_view)
        castName = itemView.findViewById(R.id.film_detail_cast_name_text_view)
        castCharacterName = itemView.findViewById(R.id.film_detail_cast_character_name_text_view)

        itemView.setOnClickListener(this)
    }

    fun bind (data: MovieCastInfo){
        castName?.text = data.name
        if (data.profileImagePath != null) {
            castImageView?.load("https://image.tmdb.org/t/p/w185${data.profileImagePath}")
        }
        else {
            castImageView?.load("https://media.istockphoto.com/id/1409329028/vector/no-picture-available-placeholder-thumbnail-icon-illustration-design.jpg?s=612x612&w=0&k=20&c=_zOuJu755g2eEUioiOUdz_mHKJQJn-tDgIAhQzyeKUQ=")
        }
        castCharacterName?.text = data.characterName

    }

    override fun onClick(v: View?) {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION){
            listener.onCastItemClicked(position)
        }
    }
}