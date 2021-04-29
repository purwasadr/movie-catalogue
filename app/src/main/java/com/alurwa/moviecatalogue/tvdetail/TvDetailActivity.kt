package com.alurwa.moviecatalogue.tvdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.alurwa.moviecatalogue.core.data.Resource
import com.alurwa.moviecatalogue.core.model.TvDetail
import com.alurwa.moviecatalogue.databinding.ActivityTvDetailBinding
import com.alurwa.moviecatalogue.detail.DetailActivity
import com.alurwa.moviecatalogue.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TvDetailActivity : AppCompatActivity() {

    private val viewModel: TvDetailViewModel by viewModels()

    private val binding: ActivityTvDetailBinding by lazy {
        ActivityTvDetailBinding.inflate(layoutInflater)
    }

    private val id: Int by lazy {
        intent.getIntExtra(Constants.EXTRA_ID, -1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(binding.root)

        setupToolbar()

        observeTv()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = ""

        }
    }

    private fun observeTv() {
        viewModel.getTvDetail(id).observe(this) {
            when (it) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    val data = it.data!!
                    setupTvView( data)
                }

                is Resource.Error -> {
                    Timber.d(it.message.toString())
                }
            }
        }
    }

    private fun setupTvView(data: TvDetail) {
        supportActionBar?.title = data.name
        binding.tvDetail = data
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}