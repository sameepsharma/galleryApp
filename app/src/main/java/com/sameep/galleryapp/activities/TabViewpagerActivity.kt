package com.sameep.galleryapp.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sameep.galleryapp.R
import com.sameep.galleryapp.adapters.MyViewPagerStateAdapter
import com.sameep.galleryapp.enums.Source
import com.sameep.galleryapp.fragments.MainFragment
import kotlinx.android.synthetic.main.activity_tab_viewpager.*

class TabViewpagerActivity : AppCompatActivity() {
    private val REQUEST_PERMISSIONS = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_viewpager)

   //     viewModel = TabViewModel(this.application)
        if (hasPermission())
        setUpTabs()
        else
            getpermission()
       // signIn()

    }

    private fun setUpTabs() {

        val adapter = MyViewPagerStateAdapter(supportFragmentManager).apply {
            //add fragments to viewPager
            val local = MainFragment(Source.LOCAL)
            val cloud = MainFragment(Source.FLICKR)

            addFragment(local, "Local Media")
            addFragment(cloud, "Cloud Media")

        }

        pager.adapter=adapter
        tab.setupWithViewPager(pager)
        //set tab icons
        tab.getTabAt(0)?.setIcon(R.drawable.ic_home_black_24dp)
        tab.getTabAt(1)?.setIcon(R.drawable.ic_cloud_foreground)
    }

    private fun hasPermission(): Boolean {

        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
                )
    }

    private fun getpermission() {
        ActivityCompat.requestPermissions(
            this,
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
                setUpTabs()
            }
        }
    }

}

