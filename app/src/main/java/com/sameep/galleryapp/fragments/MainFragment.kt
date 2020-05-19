package com.sameep.galleryapp.fragments


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidcodeman.simpleimagegallery.utils.FolderListAdapter
import com.androidcodeman.simpleimagegallery.utils.OnFolderClickListener
import com.sameep.galleryapp.R
import com.sameep.galleryapp.adapters.ChildViewPagerStateAdapter
import com.sameep.galleryapp.animations.FabAnimation
import com.sameep.galleryapp.application.GalleryApp
import com.sameep.galleryapp.dataclasses.Folder
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.enums.Source
import com.sameep.galleryapp.viewmodel.ActivityViewModel
import com.sameep.galleryapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.local_media_fragment.*
import kotlinx.android.synthetic.main.local_media_fragment.view.*

class MainFragment() : Fragment(), View.OnClickListener, OnFolderClickListener {
    var source: Source = Source.DEFAULT
    private val model: MainViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory(GalleryApp.app)
    }
    private val activityModel: ActivityViewModel by activityViewModels()
    private var isRotate = false

    constructor(source: Source) : this() {
        this.source = source
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val fragView = inflater.inflate(R.layout.local_media_fragment, container, false)
        setupTabs(fragView)

        return fragView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (source == Source.CUSTOM)
            view.local_fab.visibility = View.GONE
        else
            view.local_fab.visibility = View.VISIBLE

        view.local_fab.setOnClickListener(this)
        view.fabSave.setOnClickListener(this)
        view.fabFolder.setOnClickListener(this)
        FabAnimation.init(view.fabSave)
        FabAnimation.init(view.fabFolder)

    }

    private fun setupTabs(fragView: View) {

        val adapter = ChildViewPagerStateAdapter(source, childFragmentManager)
        //add fragments to viewPager
        if (source == Source.CUSTOM) {
            val custom = CustomFragment()
            adapter.addFragment(custom, "Folders")
        } else {
            val images = MediaFragment(MediaType.IMAGE, source)
            val videos = MediaFragment(MediaType.VIDEO, source)
            adapter.addFragment(images, "Images")
            adapter.addFragment(videos, "Videos")
        }
        /*val images = LocalImagesFragment(MediaType.IMAGE,false)
        val videos = LocalImagesFragment(MediaType.VIDEO,false)

        adapter.addFragment(images, "Images")
        adapter.addFragment(videos, "Videos")*/

        fragView.local_pager.adapter = adapter
        fragView.local_tab.setupWithViewPager(fragView.local_pager)


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.local_fab -> {
                isRotate = FabAnimation.rotateFab(v, !isRotate)
                if (isRotate) {
                    FabAnimation.showIn(fabSave)
                    FabAnimation.showIn(fabFolder)
                } else {
                    FabAnimation.showOut(fabSave)
                    FabAnimation.showOut(fabFolder)
                }
            }
            R.id.fabSave -> {
                showFolderList()
            }
            R.id.fabFolder -> {
                showCreateFolderDialog()
            }
        }

    }

    private fun showCreateFolderDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Create New Folder")
        val customView = layoutInflater.inflate(R.layout.dialog_create_folder, null)
        builder.setView(customView)
        val etFolderName = customView.findViewById<EditText>(R.id.dialog_et)
        builder.setPositiveButton("Create", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {

                if (etFolderName.text.toString() == "" || etFolderName.text.toString() == null) {
                    Toast.makeText(requireContext(), "Enter a name for folder!", Toast.LENGTH_LONG)
                        .show()
                    etFolderName.error = "Name required"
                } else {
                    val folder = Folder(
                        etFolderName.text.toString(),
                        System.currentTimeMillis(),
                        null,
                        System.currentTimeMillis()
                    )
                    model.insertIntoFolder(folder)
                    Log.e("Insert", "Yes")
                    dialog?.dismiss()
                }
            }

        })
        builder.show()
    }

    private fun showFolderList() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Folder")
        val customView = layoutInflater.inflate(R.layout.dialog_folder_list, null)
        builder.setView(customView)
        val dialogRv = customView.findViewById<RecyclerView>(R.id.dialog_rv)
        val noItems = customView.findViewById<TextView>(R.id.no_items_dialog)
        dialogRv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val dividerDecor = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        dialogRv.addItemDecoration(dividerDecor)
        var list = emptyList<Folder>()
        model.allFolders.observe(this, Observer {
            list = it
            if (list.isNotEmpty()) {
                dialogRv.visibility = View.VISIBLE
                noItems.visibility = View.GONE
            } else {
                dialogRv.visibility = View.GONE
                noItems.visibility = View.VISIBLE
            }
            val adapter = FolderListAdapter(requireContext(), list)
            adapter.onFolderClickRef=this
            dialogRv.adapter = adapter

        })
        Log.e("FolderSize>>>", "${list.size} <<<")

        builder.show()


    }

    override fun onFolderItemClick(item: Folder) {
        val selectedList = activityModel.getSharedList()
        for (i in selectedList){
            Log.e("ItemFolderBrfore>>", "${i.inFolder} <<<")
            i.inFolder=item.folderName
            Log.e("ItemFolderAfter>>", "${i.inFolder} <<<")
        }
        model.insertMediaList(selectedList)
    }
}
