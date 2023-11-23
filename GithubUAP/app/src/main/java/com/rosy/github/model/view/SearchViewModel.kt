package com.rosy.github.model.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rosy.github.api.RetrofitClient
import com.rosy.github.api.utility.Resource
import com.rosy.github.model.SearchResponse
import com.rosy.github.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel:ViewModel() {
    private val retrofit = RetrofitClient.create()
    private val listUser = MutableLiveData<Resource<List<User>>>()

    fun searchUser(query: String) : LiveData<Resource<List<User>>>{
        listUser.postValue(Resource.Loading())
        retrofit.searchUsers(query).enqueue(object : Callback<SearchResponse>{
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>,
            ) {
                val list = response.body()?.items
                if(list.isNullOrEmpty()){
                    listUser.postValue(Resource.Error(null))
                }else {
                    listUser.postValue((Resource.Success(data = list)))
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                listUser.postValue(Resource.Error(t.message))
            }
        })
        return listUser
    }
}