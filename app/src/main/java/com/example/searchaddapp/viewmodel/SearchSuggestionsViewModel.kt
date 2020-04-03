package com.example.searchaddapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.searchaddapp.model.datamodel.Address
import com.example.searchaddapp.model.repository.SearchSuggestionsRepository

class SearchSuggestionsViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: SearchSuggestionsRepository =
        SearchSuggestionsRepository(
            application
        )

    fun getSuggestionsList(queryString: String, city: String): LiveData<List<Address>> {
        return repository.callSearchSuggestionsFromApi(queryString, city)
    }

    fun getErrorOrFailMessage(): LiveData<String> {
        return repository.getErrorOrFailMessage()
    }
}