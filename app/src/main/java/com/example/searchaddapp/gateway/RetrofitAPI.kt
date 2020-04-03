package com.example.searchaddapp.gateway

import com.example.searchaddapp.model.datamodel.SearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {

    @GET("compassLocation/rest/address/autocomplete")
    fun getSearchSuggestionsResponse(
        @Query("queryString") queryString: String,
        @Query("city") city: String
    ): Observable<SearchResponse>?
}
