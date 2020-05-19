package com.sameep.galleryapp.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sameep.galleryapp.R
//View Holder for GalleryAdapter

class FolderHolder internal constructor(itemView: View) : ViewHolder(itemView) {
   val tvFolderName = itemView.findViewById<TextView>(R.id.tv_folder_name)
}