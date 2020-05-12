package com.sameep.galleryapp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.sameep.galleryapp.R
import com.sameep.galleryapp.adapters.ChildViewPagerStateAdapter
import com.sameep.galleryapp.adapters.MyViewPagerStateAdapter
import com.sameep.galleryapp.enums.MediaType
import com.sameep.galleryapp.enums.Source
import kotlinx.android.synthetic.main.local_media_fragment.view.*

class MainFragment : Fragment {
    var source: Source = Source.FLICKR

    constructor(source: Source) {
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

    private fun setupTabs(fragView: View) {

        val adapter = ChildViewPagerStateAdapter(source,childFragmentManager)
        //add fragments to viewPager
        val images = MediaFragment(MediaType.IMAGE, source)
        val videos = MediaFragment(MediaType.VIDEO, source)
        adapter.addFragment(images, "Images")
        adapter.addFragment(videos, "Videos")

        /*val images = LocalImagesFragment(MediaType.IMAGE,false)
        val videos = LocalImagesFragment(MediaType.VIDEO,false)

        adapter.addFragment(images, "Images")
        adapter.addFragment(videos, "Videos")*/

        fragView.local_pager.adapter = adapter
        fragView.local_tab.setupWithViewPager(fragView.local_pager)


    }
}
