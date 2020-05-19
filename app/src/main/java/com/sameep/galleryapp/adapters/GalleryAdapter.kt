package com.sameep.galleryapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.sameep.galleryapp.R
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.viewholders.PicHolder

interface onAdapterItemClickListener {
    fun onItemClick(item: Media)
}

interface onItemLongPressListener {
    fun onLongPress(item: Media)
}

class GalleryAdapter(
    private val pictureContx: Context,
    private val glide: RequestManager
) :
    PagedListAdapter<Media, PicHolder>(REPO_COMPARATOR) {


    var onClickRef: onAdapterItemClickListener? = null
    var onLongPressRef: onItemLongPressListener? = null

    override fun onCreateViewHolder(container: ViewGroup, position: Int): PicHolder {
        val view = LayoutInflater.from(pictureContx).inflate(R.layout.rt_gallery, container, false)
        return PicHolder(view)
    }

    override fun onBindViewHolder(holder: PicHolder, position: Int) {
        // pictureList?.let {
        val image: Media? = getItem(position)

        image?.let {

            if (it.isSelected)
                holder.rlSelected.visibility = View.VISIBLE
            else
                holder.rlSelected.visibility = View.GONE

            glide
                .load(image.thumbnailUrl)
                .apply(RequestOptions().centerCrop())
                .into(holder.picture)
            //ViewCompat.setTransitionName(holder.picture, position.toString() + "_image")

            when (image.type) {
                MediaType.IMAGE -> holder.type.setBackgroundResource(R.drawable.ic_photo)
                MediaType.VIDEO -> holder.type.setBackgroundResource(R.drawable.ic_video)
            }
            holder.name.text = image.name

            holder.picture.setOnClickListener {

                onClickRef?.onItemClick(image)


            }
            holder.picture.setOnLongClickListener {
                if (image.isSelected) {
                    holder.rlSelected.visibility = View.GONE
                    image.isSelected = false
                    notifyDataSetChanged()
                    onLongPressRef?.onLongPress(image)
                } else {
                    holder.rlSelected.visibility = View.VISIBLE
                    image.isSelected = true
                    notifyDataSetChanged()
                    onLongPressRef?.onLongPress(image)
                }
                true
            }
        }

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