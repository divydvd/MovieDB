package com.divy.moviedb

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.divy.moviedb.databinding.ActivityMovieDescBinding

class MovieDescActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMovieDescBinding
    private var movieListResult: MovieListResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_desc)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_desc)
        init()
    }

    private fun init() {
        intent?.let {
            if(it.hasExtra(MOVIE_LIST_RESULT) && it.getParcelableExtra<MovieListResult>(
                    MOVIE_LIST_RESULT) != null) {
                movieListResult = it.getParcelableExtra<MovieListResult>(
                    MOVIE_LIST_RESULT)
                movieListResult?.let { movieListResult -> populateData(movieListResult) }
                setSupportActionBar(mBinding.toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setHomeButtonEnabled(true)
                supportActionBar?.title = movieListResult?.original_title
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            else -> {

            }
        }
        return true
    }

    private fun populateData(movieListResult: MovieListResult) {
        Glide.with(this)
            .load(BuildConfig.MOVIE_DB_IMAGE_BASE_URL + movieListResult.backdrop_path)
            .into(mBinding.ivMovieImg)
        mBinding.tvReleaseDate.text = movieListResult.release_date
        mBinding.tvRatingValue.text = movieListResult.vote_average.toString()
        mBinding.tvPopularityValue.text = movieListResult.popularity.toString()
        mBinding.tvDesc.text = movieListResult.overview
    }

    companion object {
        const val MOVIE_LIST_RESULT = "MOVIE_LIST_RESULT"
        fun start(activity: Activity, movieListResult: MovieListResult) {
            val intent = Intent(activity, MovieDescActivity::class.java)
            intent.putExtra(MOVIE_LIST_RESULT, movieListResult)
            activity.startActivity(intent)
        }
    }
}