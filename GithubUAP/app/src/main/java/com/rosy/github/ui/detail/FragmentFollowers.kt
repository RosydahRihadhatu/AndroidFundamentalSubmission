package com.rosy.github.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rosy.github.R
import com.rosy.github.api.Const.EXTRA_USERNAME
import com.rosy.github.api.utility.Resource
import com.rosy.github.api.utility.stateCallback
import com.rosy.github.databinding.FollowersFragmentBinding
import com.rosy.github.model.User
import com.rosy.github.model.view.DetailViewModel
import com.rosy.github.ui.main.UserAdapter

class FragmentFollowers :Fragment(), stateCallback<List<User>> {
    private var _binding: FollowersFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailViewModel
    private lateinit var userAdapter: UserAdapter
    private var username: String? = null

    companion object{
        fun getInstance(username: String): Fragment = FragmentFollowers().apply {
            arguments = Bundle().apply { putString(EXTRA_USERNAME, username) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { username = it.getString(EXTRA_USERNAME) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FollowersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        userAdapter = UserAdapter()
        binding.followersRecyclerView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        viewModel.getFollowers(username.toString()).observe(viewLifecycleOwner){ a ->
            when(a) {
                is Resource.Error -> onFail(a.message)
                is Resource.Loading -> onLoading()
                is Resource.Success -> a.data?.let { b -> onSuccess(b as List<User>) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSuccess(data: List<User>) {
        userAdapter.setAllUser(data)
        binding.apply {
            followersRecyclerView.visibility = visible
            messageTextView.visibility = gone
            progressBar.visibility = gone
        }
    }

    override fun onLoading() {
        binding.apply {
            followersRecyclerView.visibility = gone
            messageTextView.visibility = gone
            progressBar.visibility = visible
        }
    }

    override fun onFail(message: String?) {
        binding.apply {
            if(message == null){
                messageTextView.text = resources.getString(R.string.followers_not_found)
                messageTextView.visibility = visible
            }
            messageTextView.text = message
            messageTextView.visibility = visible
            followersRecyclerView.visibility = gone
            progressBar.visibility = gone
        }
    }

}