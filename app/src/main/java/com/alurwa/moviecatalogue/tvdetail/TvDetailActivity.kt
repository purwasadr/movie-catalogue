/**
 * MIT License
 *
 * Copyright (c) 2021 Purwa Shadr Al 'urwa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.alurwa.moviecatalogue.tvdetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.moviecatalogue.core.adapter.CastAdapter
import com.alurwa.moviecatalogue.core.common.SpacingDecoration
import com.alurwa.moviecatalogue.core.data.Resource
import com.alurwa.moviecatalogue.core.model.TvDetail
import com.alurwa.moviecatalogue.databinding.ActivityTvDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TvDetailActivity : AppCompatActivity() {
    private val viewModel: TvDetailViewModel by viewModels()

    private val binding: ActivityTvDetailBinding by lazy {
        ActivityTvDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setupToolbar()

        setupRecyclerView()

        observeTv()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
    }
    private fun setupRecyclerView() {
        with(binding) {
            rcvCast.layoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rcvCast.addItemDecoration(
                SpacingDecoration(
                    16,
                    8,
                    RecyclerView.HORIZONTAL
                )
            )
            rcvCast.setHasFixedSize(true)
        }
    }

    private fun observeTv() {
        viewModel.tvDetail.observe(this) {
            when (it) {
                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    val data = it.data
                    if (data != null) {
                        setupTvView(data)
                    }
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

        binding.rcvCast.adapter = CastAdapter(data.cast)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
