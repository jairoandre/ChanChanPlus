package com.japo.chachanplus

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ImageRequester.ImageRequesterResponse {

    private var photosList : ArrayList<Photo> = ArrayList()
    private lateinit var imageRequester : ImageRequester
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter : RecyclerAdapter
    private val lastVisibleItemPosition : Int
        get() = if (recyclerView.layoutManager == linearLayoutManager) {
            linearLayoutManager.findLastVisibleItemPosition()
        } else {
            gridLayoutManager.findLastVisibleItemPosition()
        }
    private lateinit var gridLayoutManager : GridLayoutManager

    private fun setRecyclerViewScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView!!.layoutManager.itemCount
                if (!imageRequester.isLoadingData && totalItemCount == lastVisibleItemPosition + 1) {
                    requestPhoto()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onStart() {
        super.onStart()
        if (photosList.size == 0) {
            requestPhoto()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        println("Item id: ${item.itemId}")
        if (item.itemId == R.id.action_change_recycler_manager) {
            changeLayoutManager()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageRequester = ImageRequester(this)

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        adapter = RecyclerAdapter(photosList)
        recyclerView.adapter = adapter

        setRecyclerViewScrollListener()
        gridLayoutManager = GridLayoutManager(this, 2)

    }

    private fun changeLayoutManager() {
        if (recyclerView.layoutManager == linearLayoutManager) {
            recyclerView.layoutManager = gridLayoutManager
        }

        if (photosList.size == 1) {
            requestPhoto()
        } else {
            recyclerView.layoutManager = linearLayoutManager
        }

    }

    private fun requestPhoto() {
        try {
            imageRequester.getPhoto()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun receivedNewPhoto(newPhoto: Photo) {
        runOnUiThread {
            photosList.add(newPhoto)
            adapter.notifyItemInserted(photosList.size)
        }
    }
}
