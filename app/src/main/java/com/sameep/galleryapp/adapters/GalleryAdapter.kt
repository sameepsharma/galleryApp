package com.sameep.galleryapp.adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.*
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.sameep.galleryapp.R
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.fragments.MediaFragment
import com.sameep.galleryapp.viewholders.PicHolder

interface onAdapterItemClickListener {
    fun onItemClick(item: Media, mode: Boolean)
}

interface onItemLongPressListener {
    fun onLongPress(item: Media)
}

class GalleryAdapter(
    private val pictureContx: Context,
    private val glide: RequestManager
) :
    PagedListAdapter<Media, PicHolder>(REPO_COMPARATOR) {

    var inSelectionMode = false
    var listSelected: MutableList<Media> = mutableListOf()

    var onClickRef: onAdapterItemClickListener? = null
    var onLongPressRef: onItemLongPressListener? = null

    override fun onCreateViewHolder(container: ViewGroup, position: Int): PicHolder {
        val view = LayoutInflater.from(pictureContx).inflate(R.layout.rt_gallery, container, false)
        return PicHolder(view)
    }

    fun setSelectionMode(inSelection: Boolean) {
        inSelectionMode = inSelection
        if (!inSelection) {
            listSelected = mutableListOf()
            notifyDataSetChanged()
        }
    }

    fun addToList(item: Media?) {
        if (item != null)
            listSelected.add(item)
        else
            listSelected = mutableListOf()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PicHolder, position: Int) {
        val image: Media? = getItem(position)

        image?.let {
            if (listSelected.contains(image))
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

                if (inSelectionMode) {
                    if (listSelected.contains(image)) {
                        holder.rlSelected.visibility = View.GONE
                        listSelected.remove(image)
                        notifyDataSetChanged()
                    } else {
                        holder.rlSelected.visibility = View.VISIBLE
                        listSelected.add(image)
                        notifyDataSetChanged()
                    }
                }

                onClickRef?.onItemClick(image, inSelectionMode)


            }
            holder.picture.setOnLongClickListener { view ->
                if (!inSelectionMode) {
                    listSelected.add(image)
                    notifyDataSetChanged()
                }
                onLongPressRef?.onLongPress(image)
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