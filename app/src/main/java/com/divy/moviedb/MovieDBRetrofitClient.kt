package com.divy.moviedb

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MovieDBRetrofitClient(context: Context) {
    private var retrofit: Retrofit? = null
    var context: Context? = null

    init {
        this.context = context
    }

    fun getService(): APIInterface {
        context?.let {
            if (!isNetworkAvailable(it)) {
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    Toast.makeText(
                        context,
                        R.string.no_internet_message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        return getRetrofit().create(APIInterface::class.java)
    }

    fun getRetrofit(): Retrofit {
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 64
        dispatcher.maxRequestsPerHost = 5

        val builder = OkHttpClient.Builder()
        builder.connectTimeout(59, TimeUnit.SECONDS)
        builder.writeTimeout(59, TimeUnit.SECONDS)
        builder.dispatcher(dispatcher)


        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit as Retrofit
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }
}