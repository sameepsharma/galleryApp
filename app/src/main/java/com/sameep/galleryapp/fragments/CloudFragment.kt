package com.sameep.galleryapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidcodeman.simpleimagegallery.utils.CloudPhotosAdapter
import com.androidcodeman.simpleimagegallery.utils.onCloudAdapterItemClickListener
import com.sameep.galleryapp.dataclasses.PhotoModel
import com.sameep.galleryapp.R
import com.sameep.galleryapp.activities.CloudDetailActivity
import com.sameep.galleryapp.singletons.GlideProvider
import com.sameep.galleryapp.singletons.RetrofitProvider
import com.sameep.galleryapp.viewmodel.CloudViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import java.util.ArrayList


class CloudFragment : Fragment(), onCloudAdapterItemClickListener {

    private val retroObj = RetrofitProvider.getRetrofit()
    private lateinit var dashboardViewModel: CloudViewModel
    private lateinit var cloudAdapter : CloudPhotosAdapter



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            CloudViewModel()

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        setUpViews(root)
        return root
    }

    private fun setUpViews(root: View?) {

        root?.let {
            root.cloud_progress.visibility=View.VISIBLE
            root.cloud_rv.layoutManager= GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
            cloudAdapter = CloudPhotosAdapter(null, requireContext(), GlideProvider.getGlide(requireContext())).apply { onClickRef=this@CloudFragment }
            root.cloud_rv.adapter = cloudAdapter

            root.cloud_swipe.setOnRefreshListener {
                root.cloud_progress.visibility=View.VISIBLE
                dashboardViewModel.getMediaFromFlickr(retroObj)
                root.cloud_swipe.isRefreshing=false
            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cloud_progress.visibility=View.VISIBLE
        dashboardViewModel.observeFlickrResp().observe(requireActivity(), Observer {
            updateList(it, view)
        })

        dashboardViewModel.getMediaFromFlickr(retroObj)


    }

    private fun updateList(
        it: ArrayList<PhotoModel>?,
        view: View
    ) {
       cloudAdapter.setPictureList(it)
        view.cloud_progress.visibility=View.GONE
            }

    override fun onItemClick(item: PhotoModel) {

        val bundle = Bundle()
        bundle.putParcelable(CloudDetailActivity.CLOUD_DATA, item)

        /*val intent = Intent(requireContext(), CloudDetailActivity::class.java)
        intent.putExtra(CloudDetailActivity.CLOUD_DATA, item)
        */

        Navigation.findNavController(requireView()).navigate(R.id.cloud_to_detail, bundle)

    }
}
