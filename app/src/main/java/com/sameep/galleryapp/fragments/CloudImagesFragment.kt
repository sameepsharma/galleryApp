package com.sameep.galleryapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidcodeman.simpleimagegallery.utils.GalleryAdapter
import com.androidcodeman.simpleimagegallery.utils.onAdapterItemClickListener
import com.sameep.galleryapp.R
import com.sameep.galleryapp.activities.ImageDetailActivity
import com.sameep.galleryapp.dataclasses.ImageToShow
import com.sameep.galleryapp.rest.ApiInterface
import com.sameep.galleryapp.singletons.GlideProvider
import com.sameep.galleryapp.singletons.RetrofitProvider
import com.sameep.galleryapp.viewmodel.CloudViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*


class CloudImagesFragment(val isImage:Boolean) : Fragment(), onAdapterItemClickListener {

    private val retroObj = RetrofitProvider.getRetrofit()
    private lateinit var dashboardViewModel: CloudViewModel
    private lateinit var cloudAdapter : GalleryAdapter
    val client = retroObj.create(ApiInterface::class.java)



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            CloudViewModel(client)

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        setUpViews(root)
        return root
    }

    private fun setUpViews(root: View?) {

        root?.let {
            root.cloud_progress.visibility=View.VISIBLE
            root.cloud_rv.layoutManager= GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
            cloudAdapter = GalleryAdapter(null, requireContext(), GlideProvider.getGlide(requireContext())).apply { onClickRef=this@CloudImagesFragment }
            root.cloud_rv.adapter = cloudAdapter

            root.cloud_swipe.setOnRefreshListener {
                root.cloud_progress.visibility=View.VISIBLE
                dashboardViewModel.getLatestMediaFromFlickr()
                root.cloud_swipe.isRefreshing=false
            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cloud_progress.visibility=View.VISIBLE
        if (isImage){
        dashboardViewModel.observeLatestFlickr().observe(requireActivity(), Observer {
            updateList(it, view)
        })
            dashboardViewModel.getLatestMediaFromFlickr()
        }

        else
            {dashboardViewModel.observeVideos().observe(requireActivity(), Observer {
                updateList(it,view)
            })
                dashboardViewModel.getVideosFromFlickr()
            }



    }

    private fun updateList(
        it: List<ImageToShow>,
        view: View
    ) {
       cloudAdapter.setPictureList(it)
        view.cloud_progress.visibility=View.GONE
            }

    override fun onItemClick(item: ImageToShow) {

        val intent = Intent(requireContext(), ImageDetailActivity::class.java)
        intent.putExtra(ImageDetailActivity.INTENT_DATA, item)
        startActivity(intent)

    }
}
