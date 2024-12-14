package com.example.aplikasifilm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.aplikasifilm.api.FilmDetailService
import com.example.aplikasifilm.api.MovieDetail
import com.example.aplikasifilm.api.MovieDetailInfoService
import com.example.aplikasifilm.api.MovieResult
import com.example.aplikasifilm.api.model.MovieCastInfo
import com.example.aplikasifilm.api.model.MovieCreditModel
import com.example.aplikasifilm.api.model.MovieKeywordResult
import com.example.aplikasifilm.api.model.MovieRecommendationModel
import com.example.aplikasifilm.api.model.MovieSimilarResultModel
import com.example.aplikasifilm.layout_configuration.cast.CastMovieAdapter
import com.example.aplikasifilm.layout_configuration.cast.CastMovieRecycleViewClickListener
import com.example.aplikasifilm.layout_configuration.popularMovie.popularMovieRecycleViewClickListener
import com.example.aplikasifilm.layout_configuration.recommendedMovie.RecommendedMovieAdapter
import com.example.aplikasifilm.layout_configuration.recommendedMovie.RecommendedMovieRecycleViewClickListener
import com.example.aplikasifilm.layout_configuration.similarMovie.SimilarMovieAdapter
import com.example.aplikasifilm.layout_configuration.similarMovie.SimilarMovieRecycleViewClickListener
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val retrofitObject: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.themoviedb.org/3/")
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

class FilmDetailActivity :
    AppCompatActivity(),
    popularMovieRecycleViewClickListener,
    CastMovieRecycleViewClickListener,
    RecommendedMovieRecycleViewClickListener,
    SimilarMovieRecycleViewClickListener {

    val FILM_ID_EXTRAS = "com.example.aplikasifilm.FILM_ID_EXTRAS"
    var data = ArrayList<MovieResult>()
    var similarMovieData = ArrayList<MovieResult>()
    var castInfoData = ArrayList<MovieCastInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)

        val toolbar: Toolbar = findViewById(R.id.film_detail_toolbar)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val intent: Intent = intent
        val filmId: Int = intent.getIntExtra("com.example.aplikasifilm.FILM_ID_EXTRAS", -1)

        val movieTitleTextView: TextView = findViewById(R.id.film_detail_title_text_view)
        val movieOverviewTextView: TextView = findViewById(R.id.film_detail_overview_content_text_view)
        val moviePosterImageView: ImageView = findViewById(R.id.film_detail_poster_image_view)
        val movieTagline: TextView = findViewById(R.id.film_detail_tagline_text_view)

        // recommended Recycler View
        val recommendedMovieRecyclerView: RecyclerView = findViewById(R.id.film_detail_recommended_recycler_view)
        val recommendedMovieRecyclerViewViewLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recommendedMovieRecyclerView.layoutManager = recommendedMovieRecyclerViewViewLayoutManager

        // similar Recycler View
        val similarMovieRecyclerView: RecyclerView = findViewById(R.id.film_detail_similar_recycler_view)
        val similarMovieRecyclerViewViewLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        similarMovieRecyclerView.layoutManager = similarMovieRecyclerViewViewLayoutManager

        // cast recycler view
        val castRecyclerView: RecyclerView = findViewById(R.id.film_detail_cast_recycler_view)
        val castRecyclerViewLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        castRecyclerView.layoutManager = castRecyclerViewLayoutManager

        fun setRecommendationAdapter(){
            if (data.size != 0) {
                val movieRecommendationAdapter = RecommendedMovieAdapter(data, this)
                recommendedMovieRecyclerView.adapter = movieRecommendationAdapter
            }
            else {
                val recommendationLayout: LinearLayout = findViewById(R.id.film_detail_recommended_linear_layout)
                recommendationLayout.visibility = View.GONE
            }
        }

        fun setSimilarAdapter(){
            if (similarMovieData.size != 0 ) {
                val movieSimilarAdapter = SimilarMovieAdapter(similarMovieData, this)
                similarMovieRecyclerView.adapter = movieSimilarAdapter
            }
            else {
                val similarLayout: LinearLayout = findViewById(R.id.film_detail_similar_linear_layout)
                similarLayout.visibility = View.GONE
            }
        }

        fun setCastAdapter(){
            if (castInfoData.size != 0){
                val castAdapter = CastMovieAdapter(castInfoData, this)
                castRecyclerView.adapter = castAdapter
            }
        }

        fun updateUI(data: MovieDetail?){
            if (data != null) {
                movieTitleTextView.text = data.title
                movieOverviewTextView.text = data.description
                movieTagline.text = data.tagline
                moviePosterImageView.load("https://image.tmdb.org/t/p/original${data.posterPath}")
            }
        }

        runBlocking {
            // deferred item
            val movieDetailDeffered = async {
                getMovieDetailAPI(filmType = "movie", filmId = filmId)
            }
            val movieSimilarDeferred = async { getMovieSimilar(movieId = filmId) }
            val movieRecommendationDeferred = async { getMovieRecommendation(movieId = filmId) }
            val movieCreditDeferred = async { getMovieCredit(movieId = filmId) }

            val movieDetailData = movieDetailDeffered.await()
            val movieSimilar = movieSimilarDeferred.await()
            val movieRecommendation = movieRecommendationDeferred.await()
            val movieCredit = movieCreditDeferred.await()

            if (movieDetailData != null){
                updateUI(movieDetailData)
            }

            // set recommendation data
            if (movieRecommendation != null){
                for (result in movieRecommendation.results){
                    data.add(
                        MovieResult(
                            id = result.id,
                            title = result.title,
                            description = result.description,
                            posterPath = result.posterPath,
                            releaseDate = result.releaseDate,
                            voteAverage = result.voteAverage
                        )
                    )
                }
                setRecommendationAdapter()
            }

            // set similarMovieData
            if (movieSimilar != null) {
                if (movieSimilar.results.isNotEmpty()){
                    for (similarMovieResult in movieSimilar.results){
                        similarMovieData.add(
                            MovieResult(
                                id = similarMovieResult.id,
                                title = similarMovieResult.title,
                                description = similarMovieResult.description,
                                posterPath = similarMovieResult.posterPath.toString(),
                                releaseDate = similarMovieResult.releaseDate,
                                voteAverage = similarMovieResult.voteAverage
                            )
                        )
                    }
                    setSimilarAdapter()
                }
            }

            // set CastInfoData
            if (movieCredit != null){
                for (movieCreditData in movieCredit.casts){
                    castInfoData.add(movieCreditData)
                }
                setCastAdapter()
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

    override fun onCastItemClicked(position: Int) {
        Log.d("OnCastItemClicked", "onCastItemClicked: pos: $position")
    }

    override fun onRecommendedItemClicked(position: Int) {
        Log.d("OnRecommendedItemClicked", "onRecommendedItemClicked: pos: $position")
        val filmId: Int = data[position].id
        val intent: Intent = Intent(this, FilmDetailActivity::class.java)
        intent.putExtra(FILM_ID_EXTRAS, filmId)
        startActivity(intent)
    }

    override fun onSimilarItemClicked(position: Int) {
        Log.d("OnSimilarItemClicked", "onRecommendedItemClicked: pos: $position")
        val filmId: Int = similarMovieData[position].id
        val intent: Intent = Intent(this, FilmDetailActivity::class.java)
        intent.putExtra(FILM_ID_EXTRAS, filmId)
        startActivity(intent)
    }
}

suspend fun getMovieDetailAPI(filmType: String, filmId: Int, apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): MovieDetail? {
    return try {
        val service = retrofitObject.create(FilmDetailService::class.java)
        service.getFilmDetail(
            filmType = filmType,
            filmId = filmId,
            apiKey = apiKey
        )
    } catch (e: HttpException) {
        Log.e("HTTP_ERROR", "getMovieDetailAPI: HTTP ${e.code()} ${e.message()}: ${e.response()?.errorBody()}")
        null
    }
}

suspend fun getMovieRecommendation(movieId: Int, apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): MovieRecommendationModel? {
    return try {
        val service = retrofitObject.create(MovieDetailInfoService::class.java);
        service.getRecommendation(
            movieId = movieId,
            apiKey = apiKey
        )
    } catch (e: HttpException) {
        Log.e("HTTP_ERROR", "getMovieRecommendation: HTTP ${e.code()} ${e.message()}: ${e.response()?.errorBody()}")
        null
    }
}

suspend fun getMovieSimilar(movieId: Int, apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): MovieSimilarResultModel? {
    return try {
        val service = retrofitObject.create(MovieDetailInfoService::class.java);
        service.getSimilarMovie(
            movieId = movieId,
            apiKey = apiKey
        )
    } catch (e: HttpException) {
        Log.e("HTTP_ERROR", "getMovieSimilar: HTTP ${e.code()} ${e.message()}: ${e.response()?.errorBody()}")
        null
    }
}

suspend fun getMovieKeyword(movieId: Int, apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): MovieKeywordResult? {
    return try {
        val service = retrofitObject.create(MovieDetailInfoService::class.java);
        service.getMovieKeyword(
            movieId = movieId,
            apiKey = apiKey
        )
    } catch (e: HttpException) {
        Log.e("HTTP_ERROR", "getMovieReview: HTTP ${e.code()} ${e.message()}: ${e.response()?.errorBody()}")
        null
    }
}

suspend fun getMovieCredit(movieId: Int, apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): MovieCreditModel? {
    return try {
        val service = retrofitObject.create(MovieDetailInfoService::class.java);
        service.getMovieCredit(
            movieId = movieId,
            apiKey = apiKey
        )
    } catch (e: HttpException) {
        Log.e("HTTP_ERROR", "getMovieRecommendation: HTTP ${e.code()} ${e.message()}: ${e.response()?.errorBody()}")
        null
    }
}
