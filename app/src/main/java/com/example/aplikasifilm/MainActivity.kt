package com.example.aplikasifilm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasifilm.api.MovieResult
import com.example.aplikasifilm.api.MovieResultPage
import com.example.aplikasifilm.api.PopularFilmService
import com.example.aplikasifilm.api.TopRatedMovieResult
import com.example.aplikasifilm.api.TopRatedMovieResultPage
import com.example.aplikasifilm.api.TopRatedMovieService
import com.example.aplikasifilm.layout_configuration.popularMovie.popularMovieAdapter
import com.example.aplikasifilm.layout_configuration.popularMovie.popularMovieRecycleViewClickListener
import com.example.aplikasifilm.layout_configuration.topRatedMovie.TopRatedMovieAdapter
import com.example.aplikasifilm.layout_configuration.topRatedMovie.TopRatedMovieRecycleViewClickListener
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MainActivity :
    AppCompatActivity(),
    popularMovieRecycleViewClickListener,
    SearchView.OnQueryTextListener,
    TopRatedMovieRecycleViewClickListener{
    val FILM_ID_EXTRAS = "com.example.aplikasifilm.FILM_ID_EXTRAS"
    val SEARCH_QUERY = "com.example.aplikasifilm.SEARCH_QUERY"
    var data = ArrayList<MovieResult>()
    var topRatedMovieData = ArrayList<TopRatedMovieResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val popularRecyclerView: RecyclerView = findViewById(R.id.most_popular_movie_recycler_view)
        val popularRecyclerViewLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        popularRecyclerView.layoutManager = popularRecyclerViewLayoutManager

        val topRatedMovieRecyclerView: RecyclerView = findViewById(R.id.top_rated_movie_recycler_view)
        val topRatedMovieRecyclerViewLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        topRatedMovieRecyclerView.layoutManager = topRatedMovieRecyclerViewLayoutManager

        val filmSearchView: SearchView = findViewById(R.id.film_search_view)
        filmSearchView.setOnQueryTextListener(this)

        fun setAdapter(){
            val moviePopularAdapter = popularMovieAdapter(data, this)
            popularRecyclerView.adapter = moviePopularAdapter
        }

        fun topRatedMovieSetAdapter(){
            val movieTopRated = TopRatedMovieAdapter(topRatedMovieData, this)
            topRatedMovieRecyclerView.adapter = movieTopRated

        }

        fun onPopularMovieClicked(view: View, popularMovie: popularMovieAdapter){
            Log.d("onPopularMovieClicked", "onPopularMovieClicked: Hello World!")
        }

        runBlocking {
//            var filmDetailDeffered = async {
//                getMovieDetailAPI(filmType = "movie", filmId = 1075794)
//            }

            val popularFilmDeffered = async {
                getPopularFilm(filmType = "movie")
            }
            val topRatedMovieDeferred = async {
                getTopRatedMovie()
            }

            // val filmDetail = filmDetailDeffered.await()
            val popularFilmPage = popularFilmDeffered.await()
            val topRatedMoviePage = topRatedMovieDeferred.await()

            // Log.d("filmDetail", "onCreate: $filmDetail")
            Log.d("popularFilmPage", "onCreate: $popularFilmPage")

            if (popularFilmPage != null) {
                data.addAll(popularFilmPage.results)
                setAdapter()
            }

            if (topRatedMoviePage != null){
                topRatedMovieData.addAll(topRatedMoviePage.results)
                topRatedMovieSetAdapter()
            }
        }
    }
    override fun onPopularItemClicked(position: Int) {
        Log.d("OnPopularItemClicked", "onPopularItemClicked: pos: $position")
        val filmId: Int = data[position].id
        val intent: Intent = Intent(this, FilmDetailActivity::class.java)
        intent.putExtra(FILM_ID_EXTRAS, filmId)
        startActivity(intent)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d("onQueryTextSubmit", "onQueryTextSubmit: $query")
        val intent = Intent(this, FilmSearchActivity::class.java)
        intent.putExtra(SEARCH_QUERY, query)
        startActivity(intent)
        return false
    }

    override fun onTopRatedItemClicked(position: Int) {
        Log.d("OnPopularItemClicked", "onPopularItemClicked: pos: $position")
        val filmId: Int = topRatedMovieData[position].id
        val intent: Intent = Intent(this, FilmDetailActivity::class.java)
        intent.putExtra(FILM_ID_EXTRAS, filmId)
        startActivity(intent)
    }
}


suspend fun getPopularFilm(filmType: String, apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): MovieResultPage? {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit_obj = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    try {
        val service = retrofit_obj.create(PopularFilmService::class.java)
        return service.getPopularFilm(
            filmType = filmType,
            apiKey = apiKey
        )
    }
    catch (e: HttpException){
        Log.e(
            "HTTP_ERROR",
            "getPopularFilm: HTTP ${e.code()} ${e.message()}: ${e.response()?.errorBody()}", )
        return null
    }
}

suspend fun getTopRatedMovie(filmType: String = "movie", apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): TopRatedMovieResultPage? {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit_obj = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    try {
        val service = retrofit_obj.create(TopRatedMovieService::class.java)
        return service.getPopularFilm(
            filmType = filmType,
            apiKey = apiKey
        )
    }
    catch (e: HttpException){
        Log.e(
            "HTTP_ERROR",
            "getPopularFilm: HTTP ${e.code()} ${e.message()}: ${e.response()?.errorBody()}", )
        return null
    }
}