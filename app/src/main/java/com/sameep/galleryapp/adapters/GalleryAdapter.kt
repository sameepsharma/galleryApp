package com.androidcodeman.simpleimagegallery.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.sameep.galleryapp.R
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.viewholders.PicHolder

interface onAdapterItemClickListener {
    fun onItemClick(item: Media)
}

class GalleryAdapter(
    private var pictureList: List<Media>,
    private val pictureContx: Context,
    private val glide: RequestManager
) :
    PagedListAdapter<Media, PicHolder>(REPO_COMPARATOR) {


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
            val image: Media? = getItem(position)

            image?.let {
                glide
                    .load(image.thumbnailUrl)
                    .apply(RequestOptions().centerCrop())
                    .into(holder.picture)
                //ViewCompat.setTransitionName(holder.picture, position.toString() + "_image")

                when(image.type){
                    MediaType.IMAGE -> holder.type.setBackgroundResource(R.drawable.ic_photo)
                    MediaType.VIDEO -> holder.type.setBackgroundResource(R.drawable.ic_video)
                }
                holder.name.text = image.name

                holder.picture.setOnClickListener {

                    onClickRef?.onItemClick(image)


                }
            }

        }


    }

    override fun getItemCount(): Int {
        if (pictureList != null) {
            return pictureList!!.size
        } else
            return 0
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Media>() {
            override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean =
                oldItem == newItem
        }
    }

}