package com.example.searchaddapp.model.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.searchaddapp.gateway.CommunicationManager
import com.example.searchaddapp.model.datamodel.Address
import com.example.searchaddapp.view.dto.SearchQuery
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchSuggestionsRepository(private val context: Context) {

    private val autoCompletePublishSubject = PublishRelay.create<SearchQuery>()

    private var hintSuccessMutableList: MutableLiveData<List<Address>> = MutableLiveData()

    private var hintFailMessage: MutableLiveData<String> = MutableLiveData()

    fun getErrorOrFailMessage(): LiveData<String> {
        return hintFailMessage
    }

    fun callSearchSuggestionsFromApi(): LiveData<List<Address>> {
        autoCompletePublishSubject
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap {
                CommunicationManager.getSearchResponseImplMethod(
                    context,
                    it.query,
                    it.city
                )!!
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).apply {
                subscribe({ result ->
                    hintSuccessMutableList.postValue(result.data.addressList)
                }, { t: Throwable? -> hintFailMessage.value = t.toString() })
            }

        return hintSuccessMutableList
    }

    fun onEditTextInputStateChanged(searchQuery: SearchQuery) {
        autoCompletePublishSubject.accept(searchQuery)
    }

}