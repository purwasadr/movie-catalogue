package com.alurwa.moviecatalogue.tvdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alurwa.moviecatalogue.databinding.ActivityTvDetailBinding

class TvDetailActivity : AppCompatActivity() {

    private val binding: ActivityTvDetailBinding by lazy {
        ActivityTvDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}