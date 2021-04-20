package com.alurwa.moviecatalogue.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.alurwa.moviecatalogue.R
import com.alurwa.moviecatalogue.utils.SharedPreferencesUtil
import com.alurwa.moviecatalogue.databinding.ActivityMainBinding
import com.alurwa.moviecatalogue.detail.DetailActivity
import com.alurwa.moviecatalogue.search.SearchActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val fragmentManager: FragmentManager = supportFragmentManager

    private val bottomNavigationListener = BottomNavigationView.OnNavigationItemSelectedListener {
        val fragment: Fragment

        when (it.itemId) {
            R.id.movie -> {
                fragment = MovieFragment()
                fragmentManager.beginTransaction()
                        .replace(R.id.content_main, fragment, MovieFragment.TAG)
                        .commit()
                true
            }

            R.id.tv -> {
                fragment = TvFragment()
                fragmentManager.beginTransaction()
                        .replace(R.id.content_main, fragment, TvFragment.TAG)
                        .commit()
                true
            }

            else -> false
        }
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val mViewModel by viewModels<MainViewModel>()

    override fun onCreate(
            savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBottomNavigation(savedInstanceState)
    }

    private fun setupBottomNavigation(savedInstanceState: Bundle?) {
        binding.bottomNavMain.setOnNavigationItemSelectedListener(bottomNavigationListener)

        if (savedInstanceState == null) {
            binding.bottomNavMain.selectedItemId = R.id.movie
        }
    }

    private fun getIsShowPosterPref(): Boolean {
        val pref = SharedPreferencesUtil.getSharedPreferences(applicationContext)
        return pref.getBoolean("is_show_poster", false)
    }

    private fun navigateToSearch() {
        Intent(this, SearchActivity::class.java).also {
            startActivity(it)
        }
    }

    fun navigateToDetail(extraId: Int) {
        Intent(this, DetailActivity::class.java)
            .putExtra(DetailActivity.EXTRA_ID, extraId)
            .also { startActivity(it) }
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
                        putBoolean("is_show_poster", true)
                        apply()
                    }

                } else {
                    pref.edit {
                        putBoolean("is_show_poster", false)
                        apply()
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