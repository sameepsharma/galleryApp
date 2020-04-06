package com.sameep.galleryapp.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sameep.galleryapp.R
import kotlinx.android.synthetic.main.rt_gallery.view.*

class GalleryAdapter(val context: Context, var thumbnails: Array<Bitmap?>, var names : Array<String?>
) : RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
       fun setData(position: Int){
           itemView.rt_iv.setImageBitmap(thumbnails[position])
           names[position].let {
               itemView.rt
           }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rt_gallery, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
       return thumbnails.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(position)
    }
}