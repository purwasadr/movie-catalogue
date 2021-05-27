package com.alurwa.moviecatalogue.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.moviecatalogue.core.adapter.NestedMovieAdapter
import com.alurwa.moviecatalogue.databinding.FragmentMovieBinding

/**
 * Created by Purwa Shadr Al 'urwa on 20/05/2021
 */

abstract class MovieFragmentAbstract : Fragment() {
    private var _binding: FragmentMovieBinding? = null

    private val binding get() = _binding!!

    val viewModel by activityViewModels<MainViewModel>()

    val adapter by lazy {
        NestedMovieAdapter(lifecycleScope, { id ->
            navigateToDetail(id)
        }) { which ->
            navigateToList(which)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMovieBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        setupRecyclerView()

        setupSwipeToRefresh()
    }

    abstract fun navigateToDetail(extraId: Int)

    abstract fun navigateToList(which: Int)

    private fun setupAdapter() {
        /* lifecycleScope.launchWhenCreated {
             adapter.submitData(viewModel.getFilmNestedVp())
         }

         */

        getCarousels()

    }

    private fun setupRecyclerView() {

        binding.rcvMovie.layoutManager = LinearLayoutManager(context)
        binding.rcvMovie.setHasFixedSize(true)

        binding.rcvMovie.adapter = adapter
    }

    abstract fun getCarousels()

    private fun setupSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            /*   for (i in 0 until (adapter.itemCount - 1)) {
                   val viewHolder = binding.rcvMovie.layoutManager?.findViewByPosition(i)

                   viewHolder?.findViewById<TextView>(
                       R.id.txt_title
                   )?.text = "www"
                   (viewHolder?.findViewById<RecyclerView>(
                       R.id.rcv
                   )?.adapter as? MovieAdapter)?.retry()
               }

             */
            adapter.refreshMovie()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // mViewModel.listState = binding.rcvMovie.layoutManager?.onSaveInstanceState()
        //mViewModel.chipState = binding.chipGroupMovie.checkedChipId
        _binding = null
    }
}