package com.example.searchaddapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.searchaddapp.model.datamodel.Address
import com.example.searchaddapp.model.repository.SearchSuggestionsRepository
import com.example.searchaddapp.view.dto.SearchQuery

class SearchSuggestionsViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: SearchSuggestionsRepository =
        SearchSuggestionsRepository(
            application
        )

    fun getSuggestionsList(): LiveData<List<Address>> {
        return repository.callSearchSuggestionsFromApi()
    }

    fun onEditTextInputStateChanged(searchQuery: SearchQuery) {
        repository.onEditTextInputStateChanged(searchQuery)
    }

    fun getErrorOrFailMessage(): LiveData<String> {
        return repository.getErrorOrFailMessage()
    }
}