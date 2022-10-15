package com.divy.moviedb

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MovieDBViewModel : ViewModel() {

    private val popularMovieLiveData = MutableLiveData<ApiResponseGeneric<Any>>()

    fun observePopularMovieLiveData() = popularMovieLiveData

    fun fetchMovieList(context: Context) {
        try {
            viewModelScope.launch {
                kotlin.runCatching {
                    MovieDBRetrofitClient(context).getService().getPopularMovies(BuildConfig.MOVIE_DB_API_KEY)
                }
                    .onSuccess { result ->
                        if(result.code() == 200) {
                            popularMovieLiveData.value = ApiResponseGeneric.success(result.body())
                        }
                        else {
                            popularMovieLiveData.value = ApiResponseGeneric.error(Throwable("something went wrong"))
                        }
                    }
                    .onFailure {
                        it.printStackTrace()
                        popularMovieLiveData.value = ApiResponseGeneric.error(Throwable("something went wrong"))
                    }
            }
        } catch (exp: Exception) {
            exp.printStackTrace()
        }
    }
}