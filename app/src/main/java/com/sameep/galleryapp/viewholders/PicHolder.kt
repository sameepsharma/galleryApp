package com.sameep.galleryapp.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sameep.galleryapp.R
//View Holder for GalleryAdapter

class PicHolder internal constructor(itemView: View) : ViewHolder(itemView) {
    val picture = itemView.findViewById<ImageView>(R.id.rt_iv)
    val name = itemView.findViewById<TextView>(R.id.rt_tv)
    val type = itemView.findViewById<ImageView>(R.id.rt_iv_type)

}