package com.sameep.galleryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidcodeman.simpleimagegallery.utils.CustomMediaAdapter
import com.androidcodeman.simpleimagegallery.utils.OnCustomFolderClickListener
import com.androidcodeman.simpleimagegallery.utils.onCustomItemClickListener
import com.sameep.galleryapp.R
import com.sameep.galleryapp.application.GalleryApp
import com.sameep.galleryapp.dataclasses.Folder
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.singletons.GlideProvider
import com.sameep.galleryapp.viewmodel.CustomActivityViewModel
import com.sameep.galleryapp.viewmodel.CustomViewModelFactory
import kotlinx.android.synthetic.main.activity_custom_media.*

class CustomMediaActivity : AppCompatActivity(), onCustomItemClickListener {

    companion object{
        const val INTENTKEY = "folder"
    }

    lateinit var folder : Folder

    lateinit var model : CustomActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_media)

        folder = intent.getParcelableExtra(INTENTKEY)

        model=ViewModelProvider(this, CustomViewModelFactory(GalleryApp.app, folderName = folder.folderName)).get(CustomActivityViewModel::class.java)

        setupViews()
        setUpObserverAndAdapter()

    }

    private fun setUpObserverAndAdapter() {

        model.observeMedia().observe(this, Observer {
            val adapter = CustomMediaAdapter(it, this, GlideProvider.getGlide(this))
            adapter.onClickRef=this
            act_custom_rv.adapter=adapter
        })

    }

    private fun setupViews() {
        act_custom_rv.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
    }

    override fun onItemClick(item: Media) {

        val intent=Intent(this, ImageDetailActivity::class.java)
        intent.putExtra(ImageDetailActivity.INTENT_DATA, item)
        startActivity(intent)

    }

}
