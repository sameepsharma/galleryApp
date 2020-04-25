package com.sameep.galleryapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sameep.galleryapp.R
import com.sameep.galleryapp.adapters.MyViewPagerStateAdapter
import com.sameep.galleryapp.fragments.CloudFragment
import com.sameep.galleryapp.fragments.LocalMediaFragment
import com.sameep.galleryapp.viewmodel.TabViewModel
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

        val adapter = MyViewPagerStateAdapter(supportFragmentManager)
        //add fragments to viewPager
        val local = LocalMediaFragment()
        val cloud = CloudFragment()
        adapter.addFragment(local, "Local Media")
        adapter.addFragment(cloud, "Cloud Media")

        pager.adapter=adapter
        tab.setupWithViewPager(pager)
        //set tab icons
        tab.getTabAt(0)?.setIcon(R.drawable.ic_home_black_24dp)
        tab.getTabAt(1)?.setIcon(R.drawable.ic_cloud_foreground)



    }
}

