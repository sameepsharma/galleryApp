package com.sameep.galleryapp.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sameep.galleryapp.R
//View Holder for GalleryAdapter

class PicHolder internal constructor(itemView: View) : ViewHolder(itemView) {
    val picture: ImageView = itemView.findViewById<ImageView>(R.id.rt_iv)
    val name: TextView = itemView.findViewById<TextView>(R.id.rt_tv)
    val type: ImageView = itemView.findViewById<ImageView>(R.id.rt_iv_type)
    val rlSelected : RelativeLayout=itemView.findViewById(R.id.rl_tick_bg)

}