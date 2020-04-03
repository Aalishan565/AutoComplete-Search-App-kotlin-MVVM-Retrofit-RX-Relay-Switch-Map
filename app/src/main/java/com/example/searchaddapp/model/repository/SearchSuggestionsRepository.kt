package com.example.searchaddapp.model.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.searchaddapp.gateway.CommunicationManager
import com.example.searchaddapp.model.datamodel.Address
import com.example.searchaddapp.model.datamodel.SearchResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchSuggestionsRepository(private val context: Context) {

    private var hintSuccessMutableList: MutableLiveData<List<Address>> = MutableLiveData()

    private var hintFailMessage: MutableLiveData<String> = MutableLiveData()

    fun getErrorOrFailMessage(): LiveData<String> {
        return hintFailMessage
    }

    fun callSearchSuggestionsFromApi(queryString: String, city: String): LiveData<List<Address>> {
        CommunicationManager.getSearchResponseImplMethod(context, queryString, city)!!
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).apply {
                subscribe(object :
                    Observer<SearchResponse> {

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(response: SearchResponse) {
                        if (response.data.addressList.isNotEmpty()) {
                            hintSuccessMutableList.postValue(response.data.addressList)
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e != null) {
                            hintFailMessage.postValue(e.message)
                        }
                    }

                    override fun onComplete() {

                    }
                })
            }

        return hintSuccessMutableList
    }

}