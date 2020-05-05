package com.sameep.galleryapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidcodeman.simpleimagegallery.utils.GalleryAdapter
import com.androidcodeman.simpleimagegallery.utils.onAdapterItemClickListener
import com.sameep.galleryapp.R
import com.sameep.galleryapp.activities.ImageDetailActivity
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.singletons.GlideProvider
import com.sameep.galleryapp.viewmodel.LocalMediaViewModel
import com.sameep.galleryapp.viewmodel.LocalViewModelFactory
import kotlinx.android.synthetic.main.fragment_images.*
import kotlinx.android.synthetic.main.fragment_images.view.*

/**
 * A simple [Fragment] subclass.
 */
class LocalImagesFragment(val mediaType:MediaType) : Fragment(), onAdapterItemClickListener {

    lateinit var galleryAdapter: GalleryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragView = inflater.inflate(R.layout.fragment_images, container, false)

        //viewModel = LocalMediaViewModel(requireActivity().application, isImage)
        val localViewModel : LocalMediaViewModel by viewModels<LocalMediaViewModel> {
            LocalViewModelFactory(requireActivity().application, mediaType)
        }

        setupViews(fragView, localViewModel)


        return fragView
    }

    private fun updateList(data: List<Media>) {

        galleryAdapter.setPictureList(data)
        frag_loader.visibility = View.GONE
    }

    private fun setupViews(
        fragView: View,
        localViewModel: LocalMediaViewModel
    ) {
        fragView.frag_loader.visibility = View.VISIBLE

        val layoutManager = GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false)
        fragView.frag_rv.layoutManager = layoutManager
        fragView.frag_rv.hasFixedSize()
        galleryAdapter = GalleryAdapter(emptyList(), requireContext(), GlideProvider.getGlide(requireContext())).apply {
            onClickRef=this@LocalImagesFragment
        }
        fragView.frag_rv.adapter = galleryAdapter

        localViewModel.observeAllMedia().observe(requireActivity(), Observer {
            updateList(it)
        })

        fragView.frag_swipe.setOnRefreshListener {
            fragView.frag_loader.visibility = View.VISIBLE
            localViewModel.getMedia()
            fragView.frag_swipe.isRefreshing = false
        }

    }



    override fun onItemClick(image: Media) {

        val bundle = Bundle()
        bundle.putParcelable(ImageDetailActivity.INTENT_DATA, image)

        //Navigation.findNavController(requireView()).navigate(R.id.home_to_detail, bundle)


        // if (image.mediaType==1)
        val intent = Intent(activity, ImageDetailActivity::class.java)
        /* else{ // Some other target activity
              }*/

        intent.putExtra(ImageDetailActivity.INTENT_DATA, image)
        startActivity(intent)
    }

}
