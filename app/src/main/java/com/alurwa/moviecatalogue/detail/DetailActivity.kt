package com.alurwa.moviecatalogue.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.moviecatalogue.core.data.Resource
import com.alurwa.moviecatalogue.core.model.FilmDetail
import com.alurwa.moviecatalogue.databinding.ActivityDetailBinding
import com.alurwa.moviecatalogue.utils.Constants.EXTRA_ID
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailBinding by lazy {
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

        getFilmDetail()
    }

    private fun getFilmDetail() {
        mViewModel.getMovieDetail(id).observe(this) {
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
        const val EXTRA_FILM_OR_TV = "extra_film_or_tv"
        const val TAG = "DetailActivity"
    }
}