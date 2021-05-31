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

package com.alurwa.moviecatalogue.core.adapter

import android.content.res.Resources
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alurwa.moviecatalogue.core.model.CarouselMenu
import com.alurwa.moviecatalogue.databinding.ListNestedCarouselItemBinding
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by Purwa Shadr Al 'urwa on 23/05/2021
 */

class NestedMovieAdapter(
    private val scope: CoroutineScope,
    private val onClickCallback: (position: Int) -> Unit,
    private val onClickHeaderCallback: (which: Int) -> Unit
) : RecyclerView.Adapter<NestedMovieAdapter.ViewHolder>() {

    //  private val viewPool = RecyclerView.RecycledViewPool()

    private lateinit var arrayAdapter: Array<MovieAdapter>

    private val carousels: MutableList<CarouselMenu> = mutableListOf()

    fun submitData(vpData: List<CarouselMenu>) {

        arrayAdapter = Array(vpData.size) {
            MovieAdapter() { pos ->
                onClickCallback.invoke(pos)
            }
        }

        arrayAdapter.forEachIndexed { index, data ->
            scope.launch {
                data.submitData(vpData[index].pagingData)
            }
        }

        carousels.clear()
        carousels.addAll(vpData)

        /*   arrayAdapter = Array(vpList.size) {
               MovieAdapter() {

               }
           }

         */

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListNestedCarouselItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = arrayAdapter.size

    inner class ViewHolder(
        private val binding: ListNestedCarouselItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val adapter = arrayAdapter[position]

            val carouselMenu = carousels[position]

            with(binding) {

                txtTitle.text = carouselMenu.title

                rcv.layoutManager = LinearLayoutManager(
                    binding.root.context,
                    LinearLayoutManager.HORIZONTAL, false
                )

                rcv.setHasFixedSize(true)

                GravitySnapHelper(Gravity.START).also {
                    it.snapToPadding = true
                }.attachToRecyclerView(rcv)

                rcv.adapter = adapter

                //  rcv.setRecycledViewPool(viewPool)

                /*  scope.launch() {
                      adapter.submitData(carouselMenu.pagingData)
                  }

                 */

                llHeader.setOnClickListener {
                    onClickHeaderCallback.invoke(position)
                }
            }
        }
    }

    fun refreshMovie() {
        arrayAdapter.forEach {
            it.retry()
        }
    }

    private val Int.dpToPx: Int
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()

    private val Int.pxToDp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()
}
