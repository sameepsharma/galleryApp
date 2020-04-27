package com.sameep.galleryapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sameep.galleryapp.R
import com.sameep.galleryapp.adapters.MyViewPagerStateAdapter
import com.sameep.galleryapp.fragments.CloudFragment
import com.sameep.galleryapp.fragments.CloudImagesFragment
import com.sameep.galleryapp.fragments.LocalMediaFragment
import kotlinx.android.synthetic.main.activity_tab_viewpager.*

class TabViewpagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_viewpager)

   //     viewModel = TabViewModel(this.application)
        setUpTabs()
       // signIn()

    }

    private fun setUpTabs() {

        val adapter = MyViewPagerStateAdapter(supportFragmentManager).apply {
            //add fragments to viewPager
            val local = LocalMediaFragment()
            val cloud = CloudFragment()
            addFragment(local, "Local Media")
            addFragment(cloud, "Cloud Media")

        }

        pager.adapter=adapter
        tab.setupWithViewPager(pager)
        //set tab icons
        tab.getTabAt(0)?.setIcon(R.drawable.ic_home_black_24dp)
        tab.getTabAt(1)?.setIcon(R.drawable.ic_cloud_foreground)



    }
}

