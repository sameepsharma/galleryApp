package com.sameep.galleryapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidcodeman.simpleimagegallery.utils.GalleryAdapter
import com.androidcodeman.simpleimagegallery.utils.onAdapterItemClickListener
import com.sameep.galleryapp.R
import com.sameep.galleryapp.activities.ImageDetailActivity
import com.sameep.galleryapp.application.GalleryApp
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.rest.ApiInterface
import com.sameep.galleryapp.singletons.GlideProvider
import com.sameep.galleryapp.singletons.RetrofitProvider
import com.sameep.galleryapp.viewmodel.CloudViewModel
import com.sameep.galleryapp.viewmodel.CloudViewModelFactory
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*


class CloudImagesFragment(val mediaType:Int) : Fragment(), onAdapterItemClickListener {

    private val retroObj = RetrofitProvider.getRetrofit()
  //  private lateinit var dashboardViewModel: CloudViewModel
    private lateinit var cloudAdapter : GalleryAdapter
    private val client = retroObj.create(ApiInterface::class.java)



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        setUpViews(root)
        return root
    }

    private fun setUpViews(root: View?) {

        root?.let {
            val cloudViewModel : CloudViewModel by viewModels<CloudViewModel>(){

                CloudViewModelFactory(RetrofitProvider.getRetrofit().create(ApiInterface::class.java), mediaType)
            }
            root.cloud_progress.visibility=View.VISIBLE
            root.cloud_rv.layoutManager= GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
            cloudAdapter = GalleryAdapter(null, requireContext(), GlideProvider.getGlide(requireContext())).apply { onClickRef=this@CloudImagesFragment }
            root.cloud_rv.adapter = cloudAdapter

            cloudViewModel.observeMedia().observe(requireActivity(), Observer {
                updateList(it,root)
            })
            //cloudViewModel.getLatestMediaFromFlickr()

            root.cloud_swipe.setOnRefreshListener {
                root.cloud_progress.visibility=View.VISIBLE
                cloudViewModel.getLatestMediaFromFlickr()
                root.cloud_swipe.isRefreshing=false
            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cloud_progress.visibility=View.VISIBLE

                //dashboardViewModel.getLatestMediaFromFlickr()




    }

    private fun updateList(
        it: List<Media>,
        view: View
    ) {
       cloudAdapter.setPictureList(it)
        view.cloud_progress.visibility=View.GONE
            }

    override fun onItemClick(item: Media) {

        val intent = Intent(requireContext(), ImageDetailActivity::class.java)
        intent.putExtra(ImageDetailActivity.INTENT_DATA, item)
        startActivity(intent)

    }
}
