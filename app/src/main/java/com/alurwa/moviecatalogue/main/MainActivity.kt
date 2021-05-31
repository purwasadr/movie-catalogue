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

package com.alurwa.moviecatalogue.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.alurwa.moviecatalogue.R
import com.alurwa.moviecatalogue.core.common.FilmOrTv
import com.alurwa.moviecatalogue.databinding.ActivityMainBinding
import com.alurwa.moviecatalogue.search.SearchActivity
import com.alurwa.moviecatalogue.utils.Constants
import com.alurwa.moviecatalogue.utils.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBottomNavigation(savedInstanceState)
    }

    private fun setupBottomNavigation(savedInstanceState: Bundle?) {
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.filmFragment, R.id.tvFragment),
        )

        if (savedInstanceState == null) {
            binding.bottomNavMain.selectedItemId = R.id.tvFragment
        }

        binding.bottomNavMain.setupWithNavController(navController)
    }

    private fun getIsShowPosterPref(): Boolean {
        return SharedPreferencesUtil.getIsShowPosterPreferences(applicationContext)
    }

    private fun navigateToSearch() {
        val code = if (binding.bottomNavMain.selectedItemId == R.id.filmFragment) {
            FilmOrTv.FILM.code
        } else if (binding.bottomNavMain.selectedItemId == R.id.tvFragment) {
            FilmOrTv.TV.code
        } else {
            throw IllegalArgumentException(TAG)
        }

        Intent(this, SearchActivity::class.java)
            .putExtra(Constants.EXTRA_ID, code)
            .also {
                startActivity(it)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu?.findItem(R.id.show_poster)?.isChecked = getIsShowPosterPref()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_main -> {
                navigateToSearch()
                true
            }

            R.id.show_poster -> {
                val pref = SharedPreferencesUtil.getSharedPreferences(applicationContext)

                item.isChecked = !item.isChecked

                if (item.isChecked) {
                    pref.edit {
                        putBoolean(Constants.SharedPreference.IS_SHOW_POSTER, true)
                    }
                } else {
                    pref.edit {
                        putBoolean(Constants.SharedPreference.IS_SHOW_POSTER, false)
                    }
                }
                true
            }
            else -> false
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
