package com.androidcodeman.simpleimagegallery.utils

import android.app.Activity
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
import com.sameep.galleryapp.viewholders.PicHolder
import com.sameep.galleryapp.dataclasses.PictureFacer
import com.sameep.galleryapp.singletons.GlideInstance

open interface onAdapterItemClickListener {
    fun onItemClick(item: PictureFacer)
}
class GalleryAdapter(var pictureList: List<PictureFacer>?, val pictureContx: Context) : RecyclerView.Adapter<PicHolder>(){


    var onClickRef: onAdapterItemClickListener? = null

    var glide = GlideInstance.getGlide(pictureContx)

    override fun onCreateViewHolder(container: ViewGroup, position: Int): PicHolder {
        val view = LayoutInflater.from(pictureContx).inflate(R.layout.rt_gallery, container, false)
        return PicHolder(view)
    }

    override fun onBindViewHolder(holder: PicHolder, position: Int) {
        pictureList?.let {
            val image: PictureFacer = pictureList!![position]

            glide
                .load(image.picturePath)
                .apply(RequestOptions().centerCrop())
                .into(holder.picture)
            //ViewCompat.setTransitionName(holder.picture, position.toString() + "_image")

            if (image.mediaType == 1)
                holder.type.setBackgroundResource(R.drawable.ic_photo)
            else if (image.mediaType == 3)
                holder.type.setBackgroundResource(R.drawable.ic_video)

            holder.name.text = image.picturName

            holder.picture.setOnClickListener {

                onClickRef?.onItemClick(image)


            }
        }


    }



    override fun getItemCount(): Int {
        if (pictureList != null) {
            return pictureList!!.size
        }else
            return 0
    }

}