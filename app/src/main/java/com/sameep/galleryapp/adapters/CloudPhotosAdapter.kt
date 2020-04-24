package com.androidcodeman.simpleimagegallery.utils

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.sameep.galleryapp.dataclasses.PhotoModel
import com.sameep.galleryapp.R
import com.sameep.galleryapp.dataclasses.ImageSize
import com.sameep.galleryapp.viewholders.PicHolder

interface onCloudAdapterItemClickListener {
    fun onItemClick(item: PhotoModel)
}

class CloudPhotosAdapter(
    private var pictureList: ArrayList<PhotoModel>?,
    private val pictureContx: Context,
    private val glide: RequestManager
) :
    RecyclerView.Adapter<PicHolder>() {


    var onClickRef: onCloudAdapterItemClickListener? = null

    fun setPictureList(list:ArrayList<PhotoModel>?){
        this.pictureList=list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(container: ViewGroup, position: Int): PicHolder {
        val view = LayoutInflater.from(pictureContx).inflate(R.layout.rt_cloud, container, false)
        return PicHolder(view)
    }

    override fun onBindViewHolder(holder: PicHolder, position: Int) {
        pictureList?.let {
            val image: PhotoModel = it[position]
            val url =  "https://farm${image
                .farm}.staticflickr.com/${image.server}/${image.id}_${image.secret}_z.jpg"
            if (position==0)
            Log.e("URL>>", url +"<<<")

            glide
                .load(url)
                .apply(RequestOptions().centerCrop())
                .into(holder.picture)
            //ViewCompat.setTransitionName(holder.picture, position.toString() + "_image")

            /*if (image.mediaType == 1)
                holder.type.setBackgroundResource(R.drawable.ic_photo)
            else if (image.mediaType == 3)
                holder.type.setBackgroundResource(R.drawable.ic_video)*/

            holder.name.text = image.title

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