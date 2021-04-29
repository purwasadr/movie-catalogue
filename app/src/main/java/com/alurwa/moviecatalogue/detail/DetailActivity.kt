package com.alurwa.moviecatalogue.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.moviecatalogue.core.common.FilmOrTv
import com.alurwa.moviecatalogue.core.data.Resource
import com.alurwa.moviecatalogue.core.model.FilmDetail
import com.alurwa.moviecatalogue.core.model.TvDetail
import com.alurwa.moviecatalogue.databinding.ActivityDetailBinding
import com.alurwa.moviecatalogue.databinding.ActivityTvDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

  //  private val binding : ActivityDetailBinding by lazy {
  //      ActivityDetailBinding.inflate(layoutInflater)
  //  }

    private val mViewModel: DetailViewModel by viewModels()

    private val id: Int by lazy {
        intent.getIntExtra(EXTRA_ID, -1)
    }

    private val mFilmOrTv: Int by lazy {
        intent.getIntExtra(EXTRA_FILM_OR_TV, -1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (mFilmOrTv == FilmOrTv.FILM.code) {
            val binding = ActivityDetailBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setSupportActionBar(binding.toolbar)
            supportActionBar?.run {
                setDisplayHomeAsUpEnabled(true)
                title = ""

            }
            getMovieDetail(binding)
        } else {
            val binding = ActivityTvDetailBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setSupportActionBar(binding.toolbar)
            supportActionBar?.run {
                setDisplayHomeAsUpEnabled(true)
                title = ""

            }

            getTvDetail(binding)

        }
    //    setupToolbar()


    }

    private fun getMovieDetail(binding: ActivityDetailBinding) {
            mViewModel.getMovieDetail(id).observe(this) {
                when (it) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        val data = it.data!!
                        setupMovieView(binding, data)
                    }

                    is Resource.Error -> {
                        Timber.d(it.message.toString())
                    }
                }
            }

    }

    private fun getTvDetail(binding: ActivityTvDetailBinding) {
        mViewModel.getTvDetail(id).observe(this) {
            when (it) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    val data = it.data!!
                    setupTvView(binding, data)
                }

                is Resource.Error -> {
                    Timber.d(it.message.toString())
                }
            }
        }
    }

    private fun setupMovieView(binding: ActivityDetailBinding, data: FilmDetail) {
        supportActionBar?.title = data.title

        binding.movieDetail = data
    }

    private fun setupTvView(binding: ActivityTvDetailBinding,data: TvDetail) {
        supportActionBar?.title = data.name

        binding.tvDetail = data
    }

   /* private fun setupToolbar(binding) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = ""

        }
    }

    */

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
        const val EXTRA_FILM_OR_TV = "extra_film_or_tv"
        const val TAG = "DetailActivity"
    }
}