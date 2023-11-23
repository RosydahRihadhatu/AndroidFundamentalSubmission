package com.rosy.github.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rosy.github.api.Const.EXTRA_USERNAME
import com.rosy.github.api.Const.TAB_TITLES
import com.rosy.github.api.utility.Resource
import com.rosy.github.api.utility.stateCallback
import com.rosy.github.databinding.ActivityDetailUserBinding
import com.rosy.github.model.User
import com.rosy.github.model.view.DetailViewModel
import com.rosy.github.ui.main.DetailAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity(), stateCallback<User?> {
    private lateinit var binding: ActivityDetailUserBinding
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val detailAdapter = DetailAdapter(this, username.toString())

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }

        binding.apply {
            viewPager.adapter = detailAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }

        // Initialize the progressBar view
        val progressBar = binding.progressBar

        viewModel.getUserDetail(username!!).observe(this) {
            when (it) {
                is Resource.Error -> onFail(it.message)
                is Resource.Loading -> onLoading()
                is Resource.Success -> onSuccess(it.data)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onSuccess(data: User?) {
        binding.apply {
            supportActionBar?.title = data?.username
            Glide.with(this@DetailUserActivity).load(data?.avatar).into(ivPhoto)
            tvName.text = data?.name
            tvUsername.text = data?.username
            countFollowers.text = data?.followers.toString()
            countFollowing.text = data?.following.toString()

            // Hide the progressBar when loading is complete
            progressBar.visibility = View.GONE
        }
    }

    override fun onLoading() {
        binding.apply {
            // Show the progressBar when loading
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun onFail(message: String?) {
        binding.apply {
            // Hide the progressBar in case of a failure
            progressBar.visibility = View.GONE
        }
    }
}
