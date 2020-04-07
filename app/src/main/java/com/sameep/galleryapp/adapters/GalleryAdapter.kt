package com.sameep.galleryapp.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sameep.galleryapp.R
import com.sameep.galleryapp.activities.ImageDetailActivity
import kotlinx.android.synthetic.main.rt_gallery.view.*

class GalleryAdapter(
    val context: Context,
    var thumbnails: Array<Bitmap?>,
    var names: Array<String?>,
    var paths: Array<String?>,
    var type: Array<Int?>,
    var dates: Array<String?>,
    var typeMedia: Array<Int?>,
    var arrMime: Array<String?>
) : RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var pos: Int=0

        init {
            itemView.setOnClickListener{
                val intent = Intent(context, ImageDetailActivity::class.java)
                Log.e("String>>", dates[pos])
                val longTime = dates[pos]?.toLong()
                Log.e("Long>>", "$longTime <<<")
                intent.putExtra("name", names[pos])
                intent.putExtra("date", longTime)
                intent.putExtra("type", typeMedia[pos])
                intent.putExtra("mime", arrMime[pos])
                intent.putExtra("path", paths[pos])

                context.startActivity(intent)

            }
        }

       fun setData(position: Int){
           pos = position
           itemView.rt_iv.setImageBitmap(thumbnails[position])
           names[position].let {
               itemView.rt_tv.text=names[position]
       }
           if (typeMedia[position] == 3)
               itemView.rt_tv_video.visibility=View.VISIBLE

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