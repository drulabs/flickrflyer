package com.developerdru.flickflyers.ui.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.developerdru.flickflyers.R
import com.developerdru.flickflyers.data.entities.Photo
import com.developerdru.flickflyers.presentation.ViewType
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject


class HomeActivity : AppCompatActivity(), SearchView.OnQueryTextListener, HomePhotoAdapter.Listener {
    companion object {
        const val DEFAULT_SEARCH_TERM = "sunrise"
        const val SPAN_COUNT_LIST = 1
        const val SPAN_COUNT_GRID = 2
    }

    @Inject
    lateinit var homeVMFactory: HomeVMFactory

    private val homeVM: HomeVM by lazy {
        ViewModelProviders.of(this, homeVMFactory).get(HomeVM::class.java)
    }

    private val layoutManager: StaggeredGridLayoutManager by lazy {
        //GridLayoutManager(this, SPAN_COUNT_LIST)
        StaggeredGridLayoutManager(SPAN_COUNT_LIST, StaggeredGridLayoutManager.VERTICAL)
    }
    private val homeAdapter = HomePhotoAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvHomePhotos.layoutManager = layoutManager
        rvHomePhotos.adapter = homeAdapter

        homeVM.getViewState().observe(this, Observer {
            when {
                it.error != null -> {
                    progressBarHome.visibility = View.GONE
                    rvHomePhotos.visibility = View.GONE
                    tvHomeError.visibility = View.VISIBLE
                }
                it.loading -> {
                    progressBarHome.visibility = View.VISIBLE
                    rvHomePhotos.visibility = View.GONE
                    tvHomeError.visibility = View.GONE
                }
                it.data != null && it.data.isNotEmpty() -> {
                    progressBarHome.visibility = View.GONE
                    rvHomePhotos.visibility = View.VISIBLE
                    tvHomeError.visibility = View.GONE

                    layoutManager.spanCount = when (it.viewType) {
                        ViewType.LIST -> SPAN_COUNT_LIST
                        ViewType.GRID -> SPAN_COUNT_GRID
                    }

                    homeAdapter.resetPhotos(it.data)
                }
                else -> {
                    progressBarHome.visibility = View.GONE
                    rvHomePhotos.visibility = View.GONE
                    tvHomeError.visibility = View.VISIBLE
                }
            }
        })

        homeVM.searchPhotoByTag(DEFAULT_SEARCH_TERM)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.developerdru.flickflyers.R.menu.home_menu, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.action_search_tag)?.actionView as SearchView
        searchView.setSearchableInfo(
            Objects.requireNonNull(searchManager)
                .getSearchableInfo(componentName)
        )
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_view_toggle -> {
                homeVM.toggleView()
                true
            }
            else -> false
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            homeVM.searchPhotoByTag(query)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onPhotoTapped(photo: Photo) {

    }

}
