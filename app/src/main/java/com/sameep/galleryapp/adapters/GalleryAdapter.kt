package com.androidcodeman.simpleimagegallery.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sameep.galleryapp.R
import com.sameep.galleryapp.activities.ImageDetailActivity
import com.sameep.galleryapp.dataclasses.PicHolder
import com.sameep.galleryapp.dataclasses.PictureFacer
import java.util.*

class GalleryAdapter(
    pictureList: List<PictureFacer>,
    pictureContx: Context

) :
    RecyclerView.Adapter<PicHolder>() {
    private val pictureList: List<PictureFacer>
    private val pictureContx: Context

    override fun onCreateViewHolder(container: ViewGroup, position: Int): PicHolder {
        val view = LayoutInflater.from(pictureContx).inflate(R.layout.rt_gallery, container, false)
        return PicHolder(view)
    }

    override fun onBindViewHolder(holder: PicHolder, position: Int) {
        val image: PictureFacer = pictureList[position]
        Log.e("MediaType>>>", "${image.mediaType}<<<")
        Glide.with(pictureContx)
            .load(image.picturePath)
            .apply(RequestOptions().centerCrop())
            .into(holder.picture)
        ViewCompat.setTransitionName(holder.picture, position.toString() + "_image")

        if (image.mediaType==1)
            holder.type.setBackgroundResource(R.drawable.ic_photo)
        else if (image.mediaType==3)
            holder.type.setBackgroundResource(R.drawable.ic_video)

        holder.name.text = image.picturName

        holder.picture.setOnClickListener {
            val intent = Intent(pictureContx, ImageDetailActivity::class.java)

            intent.putExtra(ImageDetailActivity.dataKey.INTENT_DATA, image)

            pictureContx.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return pictureList.size
    }

    /**
     *
     * @param pictureList ArrayList of pictureFacer objects
     * @param pictureContx The Activities Context
     * @param picListerner An interface for listening to clicks on the RecyclerView's items
     */
    init {
        this.pictureList = pictureList
        this.pictureContx = pictureContx
    }
}