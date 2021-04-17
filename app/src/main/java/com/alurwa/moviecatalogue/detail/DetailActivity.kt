package com.alurwa.moviecatalogue.detail

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.moviecatalogue.core.data.Resource
import com.alurwa.moviecatalogue.core.model.MovieDetail
import com.alurwa.moviecatalogue.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

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
            }
        }
    }

    private fun setupView(data: MovieDetail) {
        val posterPath = data.posterPath
        val backdropPath = data.backdropPath

        supportActionBar?.title = data.title

        if (posterPath != null) {
            Glide.with(this)
                    .load(Uri.parse("https://image.tmdb.org/t/p/w185$posterPath"))
                    .into(binding.imgPoster)
        }

        if (backdropPath != null) {
            Glide.with(this)
                    .load(Uri.parse("https://image.tmdb.org/t/p/w500$backdropPath"))
                    .into(binding.imgBackdrop)
        }
        with(binding) {
            txtTitle.text = data.title
            txtOverviewVal.text = data.overview
            txtRuntime.text = "${data.runtime} m"
            txtVoteAvg.text = data.voteAverage.toString()
        }

        if (binding.chipGroup.childCount != 0) binding.chipGroup.removeAllViews()

        data.genres.forEach {
            val chip = Chip(binding.chipGroup.context)

            chip.text = it.name
            chip.isCheckable = false

            binding.chipGroup.addView(chip)
        }

    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)

        }
    }

    private fun strTime(time: Int): String? {
        var result = ""
        val ba = (time / 60).toDouble()
        result = if (time % 60 == 0 && time >= 60) {
            "$ba j "
        } else if (time < 60) {
            "$time m"
        } else {
            "$ba j "+(time % 60)+" m"
        }
        return result
    }
    companion object {
        const val EXTRA_ID = "EXTRA_ID"
    }
}