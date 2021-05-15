package com.alurwa.moviecatalogue.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.moviecatalogue.core.data.Resource
import com.alurwa.moviecatalogue.core.model.FilmDetail
import com.alurwa.moviecatalogue.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FilmDetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val mViewModel: FilmDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setupToolbar()

        getFilmDetail()
    }

    private fun getFilmDetail() {
        mViewModel.filmDetail.observe(this) {
            when (it) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    val data = it.data
                    if (data != null) {
                        setupMovieView(data)
                    }
                }

                is Resource.Error -> {
                    Timber.d(it.message.toString())
                }
            }
        }

    }

    private fun setupMovieView(data: FilmDetail) {
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
        const val TAG = "DetailActivity"
    }
}