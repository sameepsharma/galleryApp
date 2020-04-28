package com.androidcodeman.simpleimagegallery.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.sameep.galleryapp.R
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.viewholders.PicHolder

interface onAdapterItemClickListener {
    fun onItemClick(item: Media)
}

class GalleryAdapter(
    private var pictureList: List<Media>?,
    private val pictureContx: Context,
    private val glide: RequestManager
) :
    RecyclerView.Adapter<PicHolder>() {


    var onClickRef: onAdapterItemClickListener? = null

    fun setPictureList(list:List<Media>){
        this.pictureList=list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(container: ViewGroup, position: Int): PicHolder {
        val view = LayoutInflater.from(pictureContx).inflate(R.layout.rt_gallery, container, false)
        return PicHolder(view)
    }

    override fun onBindViewHolder(holder: PicHolder, position: Int) {
        pictureList?.let {
            val image: Media = it[position]

            glide
                .load(image.thumbnailUrl)
                .apply(RequestOptions().centerCrop())
                .into(holder.picture)
            //ViewCompat.setTransitionName(holder.picture, position.toString() + "_image")

            if (image.type == 1)
                holder.type.setBackgroundResource(R.drawable.ic_photo)
            else if (image.type == 3)
                holder.type.setBackgroundResource(R.drawable.ic_video)

            holder.name.text = image.name

            holder.picture.setOnClickListener {

                onClickRef?.onItemClick(image)


            }
        }


    }

    override fun getItemCount(): Int {
        if (pictureList != null) {
            return pictureList!!.size
        } else
            return 0
    }

}