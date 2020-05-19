package com.androidcodeman.simpleimagegallery.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sameep.galleryapp.R
import com.sameep.galleryapp.dataclasses.Folder
import com.sameep.galleryapp.viewholders.FolderHolder

interface OnFolderClickListener {
    fun onFolderItemClick(item: Folder)
}

class FolderListAdapter(
    val context: Context,
    val folders: List<Folder>
) :
    RecyclerView.Adapter<FolderHolder>() {


    var onFolderClickRef: OnFolderClickListener? = null

    override fun onCreateViewHolder(container: ViewGroup, position: Int): FolderHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rt_folder, container, false)
        return FolderHolder(view)
    }

    override fun onBindViewHolder(holder: FolderHolder, position: Int) {

        holder.tvFolderName.text=folders[position].folderName
        holder.tvFolderName.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                onFolderClickRef?.onFolderItemClick(folders[position])

            }

        })

    }

    override fun getItemCount(): Int {
        if (folders != null) {
            return folders!!.size
        } else
            return 0
    }

}