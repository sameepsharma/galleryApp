package com.sameep.galleryapp.fragments


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidcodeman.simpleimagegallery.utils.GalleryAdapter
import com.androidcodeman.simpleimagegallery.utils.onAdapterItemClickListener
import com.sameep.galleryapp.R
import com.sameep.galleryapp.activities.ImageDetailActivity
import com.sameep.galleryapp.dataclasses.PictureFacer
import com.sameep.galleryapp.singletons.GlideProvider
import com.sameep.galleryapp.viewmodel.LocalMediaViewModel
import kotlinx.android.synthetic.main.local_media_fragment.*
import kotlinx.android.synthetic.main.local_media_fragment.view.*


private const val REQUEST_PERMISSIONS = 1001
private lateinit var viewModel: LocalMediaViewModel
lateinit var galleryAdapter: GalleryAdapter


class LocalMediaFragment : Fragment(), onAdapterItemClickListener {

    companion object {
        fun newInstance() = LocalMediaFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragView = inflater.inflate(R.layout.local_media_fragment, container, false)

        viewModel = LocalMediaViewModel(requireActivity().application)

        setupViews(fragView)
        if (hasPermission()) {
            //mainViewModel.loadMedia()
            setUpObserver()

            callForMedia()
        } else
            getpermission()

        return fragView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    private fun callForMedia() {
        // CoroutineScope(IO).launch {
        viewModel.getMedia()
        //}
    }

    private fun setUpObserver() {
        viewModel.getAllMedia().observe(requireActivity(), Observer {
            updateList(it)
        })
    }

    private fun hasPermission(): Boolean {

        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
                )
    }

    private fun updateList(data: List<PictureFacer>) {

        galleryAdapter.setPictureList(data)
        frag_loader.visibility = View.GONE
    }

    private fun setupViews(fragView: View) {
        fragView.frag_loader.visibility = View.VISIBLE

        val layoutManager = GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false)
        fragView.frag_rv.layoutManager = layoutManager
        fragView.frag_rv.hasFixedSize()
        galleryAdapter = GalleryAdapter(emptyList(), requireContext(), GlideProvider.getGlide(requireContext())).apply {
            onClickRef=this@LocalMediaFragment
        }

        fragView.frag_rv.adapter = galleryAdapter


        fragView.frag_swipe.setOnRefreshListener {
            fragView.frag_loader.visibility = View.VISIBLE

            //mainViewModel.loadMedia()
            callForMedia()

            fragView.frag_swipe.isRefreshing = false
        }

    }

    private fun getpermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            REQUEST_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSIONS -> {

                //mainViewModel.loadMedia()
                setUpObserver()

                callForMedia()
            }
        }
    }

    override fun onItemClick(image: PictureFacer) {

        val bundle = Bundle()
        bundle.putParcelable(ImageDetailActivity.INTENT_DATA, image)

        Navigation.findNavController(requireView()).navigate(R.id.home_to_detail, bundle)
/*
        var intent: Intent? = null
        // if (image.mediaType==1)
        intent = Intent(activity, ImageDetailActivity::class.java)
        /* else{ // Some other target activity
              }*/

        intent?.putExtra(ImageDetailActivity.INTENT_DATA, image)

        startActivity(intent)*/
    }

}
