package com.example.aplikasifilm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasifilm.api.MovieResult
import com.example.aplikasifilm.api.MovieResultPage
import com.example.aplikasifilm.api.MovieSearchService
import com.example.aplikasifilm.layout_configuration.filmSearch.FilmSearchMovieAdapter
import com.example.aplikasifilm.layout_configuration.filmSearch.FilmSearchRecycleViewClickListener
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FilmSearchActivity : AppCompatActivity(), FilmSearchRecycleViewClickListener {
    val FILM_ID_EXTRAS = "com.example.aplikasifilm.FILM_ID_EXTRAS"
    val SEARCH_QUERY = "com.example.aplikasifilm.SEARCH_QUERY"
    private var searchData = ArrayList<MovieResult>()
    private var searchQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_search)

        // Get query from intent
        val intent = intent
        searchQuery = intent.getStringExtra(SEARCH_QUERY)

        val searchView: SearchView = findViewById(R.id.film_search_search_view)
        val searchRecyclerView: RecyclerView = findViewById(R.id.film_search_recycler_view)
        searchRecyclerView.layoutManager = GridLayoutManager(this, 2)

        // Set adapter function
        fun setSearchAdapter() {
            val searchAdapter = FilmSearchMovieAdapter(data = searchData, this)
            searchRecyclerView.adapter = searchAdapter
        }

        // Update state with search query
        fun updateState() {
            Log.d("updateState - searchQuery", "updateState: $searchQuery")
            runBlocking {
                val searchResultDeferred = async {
                    getSearchFilm(query = searchQuery ?: "")
                }

                val searchResultPage = searchResultDeferred.await()
                Log.d("updateState", "updateState: searchResultPage: $searchResultPage")
                if (searchResultPage != null) {
                    searchData = ArrayList(searchResultPage.results)
                    setSearchAdapter()
                }
            }
        }

        // Initial state update
        updateState()

        // Handle search query changes
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchQuery = query
                    updateState()
                }
                return false
            }
        })
    }

    override fun onSearchItemClicked(position: Int) {
        Log.d("OnSearchItemClicked", "onSearchItemClicked: pos: $position")
        val filmId: Int = searchData[position].id
        val intent = Intent(this, FilmDetailActivity::class.java).apply {
            putExtra(FILM_ID_EXTRAS, filmId)
        }
        startActivity(intent)
    }
}

suspend fun getSearchFilm(query: String, apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): MovieResultPage? {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofitObj = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    return try {
        val service = retrofitObj.create(MovieSearchService::class.java)
        service.getMovieSearch(
            query = query,
            apiKey = apiKey
        )
    } catch (e: HttpException) {
        Log.e(
            "HTTP_ERROR",
            "getSearchFilm: HTTP ${e.code()} ${e.message()}: ${e.response()?.errorBody()}"
        )
        null
    }
}

class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: android.graphics.Rect,
        view: android.view.View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = spacing / 2
        outRect.right = spacing / 2
        outRect.top = spacing / 2
        outRect.bottom = spacing / 2
    }
}
