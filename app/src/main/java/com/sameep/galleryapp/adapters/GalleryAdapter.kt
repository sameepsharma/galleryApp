package com.androidcodeman.simpleimagegallery.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.sameep.galleryapp.R
import com.sameep.galleryapp.dataclasses.ImageToShow
import com.sameep.galleryapp.dataclasses.PictureFacer
import com.sameep.galleryapp.viewholders.PicHolder

interface onAdapterItemClickListener {
    fun onItemClick(item: ImageToShow)
}

class GalleryAdapter(
    private var pictureList: List<ImageToShow>?,
    private val pictureContx: Context,
    private val glide: RequestManager
) :
    RecyclerView.Adapter<PicHolder>() {


    var onClickRef: onAdapterItemClickListener? = null

    fun setPictureList(list:List<ImageToShow>){
        this.pictureList=list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(container: ViewGroup, position: Int): PicHolder {
        val view = LayoutInflater.from(pictureContx).inflate(R.layout.rt_gallery, container, false)
        return PicHolder(view)
    }

    override fun onBindViewHolder(holder: PicHolder, position: Int) {
        pictureList?.let {
            val image: ImageToShow = it[position]

            glide
                .load(image.url)
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