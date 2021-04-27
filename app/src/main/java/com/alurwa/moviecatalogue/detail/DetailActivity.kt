package com.alurwa.moviecatalogue.detail

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.moviecatalogue.core.data.Resource
import com.alurwa.moviecatalogue.core.model.MovieDetail
import com.alurwa.moviecatalogue.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding : ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val mViewModel: DetailViewModel by viewModels()

    private val id: Int by lazy {
        intent.getIntExtra(EXTRA_ID, -1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setupToolbar()

        getMovieDetail()
    }

    private fun getMovieDetail() {
        mViewModel.getMovieDetail(id).observe(this) {
            when (it) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    val data = it.data!!

                    setupView(data)
                }

                is Resource.Error -> {
                    Log.d(TAG, it.message.toString())
                }
            }
        }
    }

    private fun setupView(data: MovieDetail) {
        supportActionBar?.title = data.title

        binding.movieDetail = data
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = ""

        }
    }

 /*   override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> false
        }
    }

  */

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val TAG = "DetailActivity"
    }
}