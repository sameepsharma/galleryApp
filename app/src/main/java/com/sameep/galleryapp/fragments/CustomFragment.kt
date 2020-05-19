package com.sameep.galleryapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidcodeman.simpleimagegallery.utils.CustomFragmentListAdapter
import com.androidcodeman.simpleimagegallery.utils.OnCustomFolderClickListener
import com.sameep.galleryapp.R
import com.sameep.galleryapp.activities.CustomMediaActivity
import com.sameep.galleryapp.application.GalleryApp
import com.sameep.galleryapp.dataclasses.Folder
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.enums.Source
import com.sameep.galleryapp.viewmodel.CustomDataViewModel
import kotlinx.android.synthetic.main.fragment_custom.view.*

/**
 * A simple [Fragment] subclass.
 */
class CustomFragment : Fragment(), OnCustomFolderClickListener {


    /*: CustomDataViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory(GalleryApp.app)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_custom, container, false)
        view.custom_rv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("CustomOnView>>", "YES<<<")
        val model = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(GalleryApp.app)
        ).get(CustomDataViewModel::class.java)
        model.folderList.observe(requireActivity(), Observer {
            if (it == null) {
                view.tv_no_folder.visibility = View.VISIBLE
                view.custom_rv.visibility = View.GONE
            } else {
                view.custom_rv.adapter =
                    CustomFragmentListAdapter(requireContext(), it).also { adapterRef ->
                        adapterRef.onFolderClickRef = this
                    }
            }

        })
    }

    override fun onItemClick(item: Folder) {
        val intent = Intent(requireContext(), CustomMediaActivity::class.java)
        intent.putExtra(CustomMediaActivity.INTENTKEY, item)
        requireContext().startActivity(intent)

    }


}
