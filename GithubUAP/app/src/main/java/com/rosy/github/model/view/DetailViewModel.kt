package com.rosy.github.model.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rosy.github.api.RetrofitClient
import com.rosy.github.api.utility.Resource
import com.rosy.github.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {
    private val retrofit = RetrofitClient.create()
    private val user = MutableLiveData<Resource<User>>()
    private val listFollowers = MutableLiveData<Resource<List<User>>>()
    private val listFollowing = MutableLiveData<Resource<List<User>>>()

    fun getUserDetail(username: String): LiveData<Resource<User>> {
        retrofit.getUserDetail(username).enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val results = response.body()
                user.postValue(Resource.Success(results))
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                user.postValue(Resource.Error(t.message))
            }
        })
        return user
    }

    fun getFollowers(username: String): LiveData<Resource<List<User>>> {
        listFollowers.postValue(Resource.Loading())
        retrofit.getFollowers(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                val list = response.body()
                if (list.isNullOrEmpty()) {
                    listFollowers.postValue(Resource.Error(null))
                } else {
                    listFollowers.postValue(Resource.Success(list))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                listFollowers.postValue(Resource.Error(t.message))
            }
        })
        return listFollowers
    }

    fun getFollowing(username: String): LiveData<Resource<List<User>>> {
        listFollowing.postValue(Resource.Loading())
        retrofit.getFollowing(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                val list = response.body()
                if (list.isNullOrEmpty()) {
                    listFollowing.postValue(Resource.Error(null))
                } else {
                    listFollowing.postValue(Resource.Success(list))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                listFollowing.postValue(Resource.Error(t.message))
            }
        })
        return listFollowing
    }
   
}