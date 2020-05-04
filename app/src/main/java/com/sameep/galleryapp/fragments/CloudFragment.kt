package com.sameep.galleryapp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sameep.galleryapp.R
import com.sameep.galleryapp.adapters.MyViewPagerStateAdapter
import com.sameep.galleryapp.enums.MediaType
import kotlinx.android.synthetic.main.local_media_fragment.view.*

class CloudFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragView = inflater.inflate(R.layout.local_media_fragment, container, false)

        setupTabs(fragView)

        return fragView
    }

    private fun setupTabs(fragView: View) {


        val adapter = MyViewPagerStateAdapter(childFragmentManager)
        //add fragments to viewPager
        val images = CloudImagesFragment(MediaType.IMAGE.value)
        val videos = CloudImagesFragment(MediaType.VIDEO.value)

        adapter.addFragment(images, "Images")
        adapter.addFragment(videos, "Videos")

        fragView.local_pager.adapter=adapter
        fragView.local_tab.setupWithViewPager(fragView.local_pager)


    }

}
