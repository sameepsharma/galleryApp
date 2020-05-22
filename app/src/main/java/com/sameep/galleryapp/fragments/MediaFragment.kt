package com.sameep.galleryapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sameep.galleryapp.R
import com.sameep.galleryapp.activities.ImageDetailActivity
import com.sameep.galleryapp.adapters.GalleryAdapter
import com.sameep.galleryapp.adapters.onAdapterItemClickListener
import com.sameep.galleryapp.adapters.onItemLongPressListener
import com.sameep.galleryapp.application.GalleryApp
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.enums.Source
import com.sameep.galleryapp.rest.ApiInterface
import com.sameep.galleryapp.singletons.GlideProvider
import com.sameep.galleryapp.singletons.RetrofitProvider
import com.sameep.galleryapp.viewmodel.ActivityViewModel
import com.sameep.galleryapp.viewmodel.LocalViewModelFactory
import com.sameep.galleryapp.viewmodel.MediaViewModel
import kotlinx.android.synthetic.main.fragment_images.view.*


/**
 * A simple [Fragment] subclass.
 */
class MediaFragment(
    private var mediaType: MediaType = MediaType.DEFAULT,
    private var source: Source = Source.DEFAULT
) : Fragment(),
    onAdapterItemClickListener, onItemLongPressListener {

    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var fragloader: ProgressBar
    //selectionMode
    private var inSelectionMode = false

    private val activityModel: ActivityViewModel by activityViewModels()
    private val localViewModel: MediaViewModel by viewModels<MediaViewModel>() {
        LocalViewModelFactory(
            GalleryApp.app,
            mediaType,
            RetrofitProvider.getRetrofit().create(ApiInterface::class.java),
            source
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragView = inflater.inflate(R.layout.fragment_images, container, false)
        fragloader = fragView.frag_loader

        if (source == Source.FLICKR)
            fragView.input.visibility = View.VISIBLE
        else
            fragView.input_layout.visibility = View.GONE


        setupViews(fragView)
        setupSwipeToRefresh(fragView)
        setUpObserverForMedia()
        setObserverForSelectionMode()
        initSearch(fragView, localViewModel)


        return fragView
    }

    private fun addToListInAdapter(item: Media?) {
        galleryAdapter.addToList(item)
    }

    private fun setObserverForSelectionMode() {

        activityModel.observeActionMode().observe(requireActivity(), Observer {
            galleryAdapter.setSelectionMode(it)
            inSelectionMode=it
            if (!it)
                cabMode?.finish()
        })

    }

    private fun setupSwipeToRefresh(
        fragView: View
    ) {
        fragView.frag_swipe.setOnRefreshListener {
            fragView.frag_loader.visibility = View.VISIBLE
            if (source == Source.FLICKR)
                localViewModel.searchMediaByQuery(fragView.input.text.toString())
            else if (source == Source.LOCAL)
                localViewModel.searchMediaByQuery(MediaViewModel.EMPTYQUERY)
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
        fragloader.visibility = View.GONE
    }

    private fun setupViews(fragView: View) {
        fragView.frag_loader.visibility = View.VISIBLE

        val layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        fragView.frag_rv.layoutManager = layoutManager

        galleryAdapter =
            GalleryAdapter(requireContext(), GlideProvider.getGlide(requireContext())).apply {
                onClickRef = this@MediaFragment
                onLongPressRef = this@MediaFragment
            }

        fragView.frag_rv.adapter =
            galleryAdapter//if (source==Source.FLICKR) galleryAdapter else localAdapter

    }

    override fun onItemClick(image: Media, inSelectionMode: Boolean) {

        if (inSelectionMode) {
            addToSharedList(image)
        } else {
            val bundle = Bundle()
            bundle.putParcelable(ImageDetailActivity.INTENT_DATA, image)

            val intent = Intent(activity, ImageDetailActivity::class.java)

            intent.putExtra(ImageDetailActivity.INTENT_DATA, image)
            startActivity(intent)
        }
    }

    private fun initSearch(fragView: View, model: MediaViewModel) {
        fragView.input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                Log.e("Query>> ", fragView.input.text.toString() + "<<")
                model.searchMediaByQuery(fragView.input.text.toString())
                true
            } else {
                false
            }
        }

    }
var cabMode:ActionMode?=null
    override fun onLongPress(
        item: Media
    ) {
        if (!inSelectionMode){
            activityModel.inSelectionMode(true)
            addToSharedList(item)
            cabMode = requireActivity().startActionMode(object : ActionMode.Callback {
                override fun onActionItemClicked(mode: ActionMode?, menuItem: MenuItem?): Boolean {
                    addToSharedList(item)
                    addToListInAdapter(item)
                    return true
                }

                override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    activityModel.inSelectionMode(true)
                    return true
                }

                override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    return false
                }

                override fun onDestroyActionMode(mode: ActionMode?) {
                    Log.e("Destroy>>", "Yes<<<")
                    activityModel.clearSelectedList()
                    activityModel.inSelectionMode(false)
                    addToListInAdapter(null)
                    //mode?.finish()
                }

            })

        }else
            Toast.makeText(requireContext(), "Already in Selection Mode!", Toast.LENGTH_LONG).show()

    }

    private fun addToSharedList(item: Media) {
        val list = activityModel.getSharedList()
        list?.let {
            if (!list.contains(item)) {
                activityModel.addToSharedList(item)
                Log.e("ActivityListAdd>>", "${activityModel.getSharedList().size} <<")
                //localViewModel.setValueMediatorForSelectedList(selectedMedia)
            } else {
                activityModel.deleteFromSharedList(item)
                /*if (activityModel.getSharedList().size==0)
                    activityModel.inSelectionMode(false)*/
                Log.e("ActivityListDel>>", "${activityModel.getSharedList().size} <<")

            }
        }

    }

}
