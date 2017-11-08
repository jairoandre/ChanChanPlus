package com.japo.chachanplus

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ImageRequester.ImageRequesterResponse {

    private var photosList : ArrayList<Photo> = ArrayList()
    private lateinit var imageRequester : ImageRequester
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter : RecyclerAdapter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageRequester = ImageRequester(this)

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        adapter = RecyclerAdapter(photosList)
        recyclerView.adapter = adapter
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
