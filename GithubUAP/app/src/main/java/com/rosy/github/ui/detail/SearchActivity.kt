package com.rosy.github.ui.detail

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.rosy.github.R
import com.rosy.github.api.utility.Resource
import com.rosy.github.api.utility.stateCallback
import com.rosy.github.databinding.ActivitySearchBinding
import com.rosy.github.model.User
import com.rosy.github.model.view.SearchViewModel
import com.rosy.github.ui.main.UserAdapter

class SearchActivity :AppCompatActivity(), stateCallback<List<User>> {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var  userAdapter: UserAdapter
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userAdapter = UserAdapter()
        binding.includeMainSearch.recyclerView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean{
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search_bar).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchUser(query.toString()).observe(this@SearchActivity) { a ->
                    when (a){
                        is Resource.Loading -> onLoading()
                        is Resource.Success -> a.data?.let { b -> onSuccess(b) }
                        else -> onFail(a.message)
                    }
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
        return true
    }

    override fun onSuccess(data: List<User>) {
        userAdapter.setAllUser(data)
        binding.includeMainSearch.apply {
            recyclerView.visibility = visible
            tvMessage.visibility = gone
            progressBar.visibility = gone
        }
    }

    override fun onLoading() {
        binding.includeMainSearch.apply {
            progressBar.visibility = visible
            tvMessage.visibility = gone
            recyclerView.visibility = gone
        }
    }

    override fun onFail(message: String?) {
        binding.includeMainSearch.apply {
            if (message.isNullOrEmpty()) {
                tvMessage.apply {
                    text = resources.getString(R.string.user_not_found)
                    visibility = visible
                }
            }
            tvMessage.apply {
                text = message
                visibility = visible
            }
            progressBar.visibility = gone
            recyclerView.visibility = gone
        }
    }

}