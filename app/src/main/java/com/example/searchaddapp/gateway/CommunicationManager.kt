package com.example.searchaddapp.gateway

import android.content.Context
import com.example.searchaddapp.apputils.AppUtils
import com.example.searchaddapp.model.datamodel.SearchResponse
import io.reactivex.Observable
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CommunicationManager {

    companion object {

        var api: RetrofitAPI? = null

        private fun getRetrofitInstance(context: Context): RetrofitAPI? {
            if (null == api) {
                val url: String?
                try {
                    //Need to put in build config file
                    url = "https://digi-api.airtel.in/"
                    val okHttpClient = OkHttpClient.Builder()
                        .cache(Cache(context.cacheDir, 5 * 1024 * 1024))
                        .addInterceptor { chain ->
                            var request = chain.request()
                            request = if (AppUtils.isNetworkAvailable(context)!!)
                                request.newBuilder().header("Cache-Control", "public, max-age=" + 5)
                                    .build()
                            else
                                request.newBuilder().header(
                                    "Cache-Control",
                                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                                ).build()
                            chain.proceed(request)
                        }
                        .build()
                    val retrofit = Retrofit.Builder()
                        .baseUrl(url)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
                    api = retrofit.create(RetrofitAPI::class.java)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            return api
        }

        fun getSearchResponseImplMethod(
            context: Context,
            queryString: String,
            city: String
        ): Observable<SearchResponse>? {
            return try {
                getRetrofitInstance(context)?.getSearchSuggestionsResponse(queryString, city)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
