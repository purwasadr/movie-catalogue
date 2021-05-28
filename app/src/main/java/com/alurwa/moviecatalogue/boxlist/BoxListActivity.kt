package com.alurwa.moviecatalogue.boxlist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.moviecatalogue.core.adapter.BoxMovieAdapter
import com.alurwa.moviecatalogue.core.common.SpacingDecoration
import com.alurwa.moviecatalogue.databinding.ActivityBoxListBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BoxListActivity : AppCompatActivity() {
    private val binding: ActivityBoxListBinding by lazy {
        ActivityBoxListBinding.inflate(layoutInflater)
    }

    private val adapter: BoxMovieAdapter by lazy {
        BoxMovieAdapter() {

        }
    }

    private val viewModel: BoxListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()

        getMovies()
    }

    private fun setupRecyclerView() {
        with(binding) {
            val flexboxLayoutManager = FlexboxLayoutManager(applicationContext)

            flexboxLayoutManager.flexDirection = FlexDirection.ROW
            flexboxLayoutManager.alignItems = AlignItems.BASELINE
            flexboxLayoutManager.justifyContent = JustifyContent.SPACE_AROUND

            list.setHasFixedSize(true)
            list.addItemDecoration(SpacingDecoration(16, RecyclerView.VERTICAL))
            list.layoutManager = flexboxLayoutManager
            list.adapter = adapter
            list.itemAnimator = null
        }
    }

    private fun getMovies() {
        lifecycleScope.launch {
            viewModel.movies.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}