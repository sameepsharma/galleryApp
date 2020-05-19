package com.androidcodeman.simpleimagegallery.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sameep.galleryapp.R
import com.sameep.galleryapp.dataclasses.Folder
import com.sameep.galleryapp.viewholders.FolderHolder

interface OnCustomFolderClickListener {
    fun onItemClick(item: Folder)
}

class CustomFragmentListAdapter(
    val context: Context,
    val folders: List<Folder>
) :
    RecyclerView.Adapter<CustomFragmentListAdapter.ViewHolder>() {

    var onFolderClickRef: OnCustomFolderClickListener? = null

    override fun onCreateViewHolder(container: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rt_custom, container, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvFolderName.text = folders[position].folderName
        holder.tvFolderName.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                onFolderClickRef?.onItemClick(folders[position])

            }

        })

    }

    override fun getItemCount(): Int {
        if (folders != null) {
            return folders!!.size
        } else
            return 0
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvFolderName = view.findViewById<TextView>(R.id.rt_folder_name)
    }

}