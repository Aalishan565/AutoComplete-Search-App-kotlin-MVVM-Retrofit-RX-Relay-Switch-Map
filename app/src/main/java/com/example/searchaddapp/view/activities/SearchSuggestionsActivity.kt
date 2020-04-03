package com.example.searchaddapp.view.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.searchaddapp.R
import com.example.searchaddapp.apputils.AppUtils
import com.example.searchaddapp.view.adapters.SearchSuggestionsAdapter
import com.example.searchaddapp.view.dto.SearchQuery
import com.example.searchaddapp.viewmodel.SearchSuggestionsViewModel
import kotlinx.android.synthetic.main.activity_search.*


class SearchSuggestionsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    AdapterView.OnItemClickListener, TextWatcher {

    private lateinit var selectedCity: String
    private lateinit var searchSuggestionsViewModel: SearchSuggestionsViewModel
    private lateinit var adapter: SearchSuggestionsAdapter
    private var addressList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchSuggestionsViewModel =
            ViewModelProvider(this).get(SearchSuggestionsViewModel::class.java)
        adapter = SearchSuggestionsAdapter(
            this, android.R.layout.simple_dropdown_item_1line
        )
        actvQueary.threshold = 2
        actvQueary.setAdapter(adapter)
        actvQueary.onItemClickListener = this
        actvQueary.addTextChangedListener(this)
        spinnerCities.onItemSelectedListener = this
        observeData()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedCity = parent?.getItemAtPosition(position).toString()
    }

    private fun observeData() {
        searchSuggestionsViewModel.getSuggestionsList().observe(
            this,
            Observer { t ->
                if (t.isNotEmpty()) {
                    addressList.clear()
                    for ((index) in t.withIndex()) {
                        addressList.add(t[index].addressString)
                    }
                    adapter.setData(addressList)
                    adapter.notifyDataSetChanged()
                    hideProgressBar()
                }
            })
        searchSuggestionsViewModel.getErrorOrFailMessage().observe(this, Observer { t ->
            hideProgressBar()
            AppUtils.showToastMessage(this, t.toString())
        })
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        AppUtils.hideKeyboard(this, view)
        AppUtils.showToastMessage(this, addressList[position])
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        showProgressBar()
        searchSuggestionsViewModel.onEditTextInputStateChanged(
            SearchQuery(
                s.toString(),
                selectedCity
            )
        )

    }

    private fun hideProgressBar() {
        if (null != progressBar && progressBar.isVisible) {
            progressBar.visibility = View.GONE
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }
}

