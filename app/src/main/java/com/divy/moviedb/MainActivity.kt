package com.divy.moviedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.divy.moviedb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MovieListAdapter.MovieItemClickListener {

    lateinit var mBinding: ActivityMainBinding
    lateinit var viewModel: MovieDBViewModel
    private var movieListAdapter: MovieListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MovieDBViewModel::class.java]
        init()
    }

    private fun init() {
        viewModel.fetchMovieList(this)
        viewModel.observePopularMovieLiveData().observe(this) {
            handleMovieResponseData(it)
        }
    }

    private fun handleMovieResponseData(it: ApiResponseGeneric<Any>?) {
        if (isFinishing) return
        when(it?.status) {
            Status.SUCCESS -> {
                try {
                    it.data?.let { data ->
                        val response = data as PopularMoviesResponse
                        setupAdapter(response)
                    }
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            Status.LOADING -> {

            }
            else -> {

            }
        }
    }

    private fun setupAdapter(response: PopularMoviesResponse) {
        movieListAdapter = MovieListAdapter(response.results, this)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.rvMovieList.apply {
            adapter = movieListAdapter
            layoutManager = linearLayoutManager
        }
        movieListAdapter?.notifyDataSetChanged()
    }

    override fun onMovieItemClicked(result: MovieListResult) {
        MovieDescActivity.start(this, result)
    }
}