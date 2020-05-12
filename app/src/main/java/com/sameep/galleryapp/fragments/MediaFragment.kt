package com.sameep.galleryapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sameep.galleryapp.R
import com.sameep.galleryapp.activities.ImageDetailActivity
import com.sameep.galleryapp.adapters.GalleryAdapter
import com.sameep.galleryapp.adapters.onAdapterItemClickListener
import com.sameep.galleryapp.application.GalleryApp
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.enums.Source
import com.sameep.galleryapp.rest.ApiInterface
import com.sameep.galleryapp.singletons.GlideProvider
import com.sameep.galleryapp.singletons.RetrofitProvider
import com.sameep.galleryapp.viewmodel.LocalViewModelFactory
import com.sameep.galleryapp.viewmodel.MediaViewModel
import kotlinx.android.synthetic.main.fragment_images.*
import kotlinx.android.synthetic.main.fragment_images.view.*


/**
 * A simple [Fragment] subclass.
 */
class MediaFragment(private val mediaType:MediaType, private val source: Source) : Fragment(),
    onAdapterItemClickListener {

    private lateinit var galleryAdapter: GalleryAdapter
    private var hasLoadedOnce=false
    //private lateinit var localAdapter : LocalMeddiaAdapter

    private val localViewModel : MediaViewModel by viewModels<MediaViewModel>() {
        LocalViewModelFactory(GalleryApp.app, mediaType, RetrofitProvider.getRetrofit().create(ApiInterface::class.java), source)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(true)
        if (this.isVisible) {
            if (isVisibleToUser && !hasLoadedOnce){

                hasLoadedOnce=true
            }

        }
        }


    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isFragmentVisible(this)){
            setupViews(view)
            setupSwipeToRefresh(view)
            setUpObserverForMedia()
            initSearch(view,localViewModel)

        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragView = inflater.inflate(R.layout.fragment_images, container, false)

        if (source == Source.FLICKR)
            fragView.input.visibility = View.VISIBLE
        else
            fragView.input_layout.visibility = View.GONE


            setupViews(fragView)
            setupSwipeToRefresh(fragView)
            setUpObserverForMedia()
            initSearch(fragView,localViewModel)


        return fragView
    }

    fun isFragmentVisible(fragment: Fragment): Boolean {
        val activity: Activity? = fragment.activity
        val focusedView = fragment.requireView()!!.findFocus()
        return activity != null && focusedView != null && focusedView === activity.window.decorView.findFocus()
    }
    private fun setupSwipeToRefresh(
        fragView: View
    ) {
        fragView.frag_swipe.setOnRefreshListener {
            fragView.frag_loader.visibility = View.VISIBLE
            if (source==Source.FLICKR)
                localViewModel.searchMediaByQuery(fragView.input.text.toString())
            else
                localViewModel.fetchLocalMedia()
            fragView.frag_swipe.isRefreshing = false
        }

    }

    private fun setUpObserverForMedia() {
        localViewModel.mediator.observe(requireActivity(), Observer {
            updateList(it)
        })
    }

    private fun updateList(data: PagedList<Media>) {
        galleryAdapter.submitList(data)
        frag_loader.visibility = View.GONE
    }

    private fun setupViews(fragView: View) {
        fragView.frag_loader.visibility = View.VISIBLE

        val layoutManager = GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false)
        fragView.frag_rv.layoutManager = layoutManager
        //fragView.frag_rv.hasFixedSize()

        galleryAdapter = GalleryAdapter(requireContext(), GlideProvider.getGlide(requireContext())).apply {
            onClickRef=this@MediaFragment
        }

        fragView.frag_rv.adapter =galleryAdapter//if (source==Source.FLICKR) galleryAdapter else localAdapter


    }

    override fun onItemClick(image: Media) {

        val bundle = Bundle()
        bundle.putParcelable(ImageDetailActivity.INTENT_DATA, image)

        val intent = Intent(activity, ImageDetailActivity::class.java)

        intent.putExtra(ImageDetailActivity.INTENT_DATA, image)
        startActivity(intent)
    }

    private fun initSearch(fragView: View, model: MediaViewModel) {
        fragView.input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                Log.e("Query>> ", fragView.input.text.toString()+"<<")
               // model.invalidateData()

                model.searchMediaByQuery(fragView.input.text.toString())
                /*model.flickrMedia.observe(requireActivity(), Observer {
                    updateList(it)
                })*/
                true
            } else {
                false
            }
        }

    }

}
