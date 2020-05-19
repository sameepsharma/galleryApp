package com.sameep.galleryapp.adapters

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sameep.galleryapp.enums.Source
import com.sameep.galleryapp.fragments.CustomFragment
import com.sameep.galleryapp.fragments.MediaFragment
import okhttp3.MediaType

class ChildViewPagerStateAdapter(val source: Source, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentList = mutableListOf<Fragment>()
    private val fragmentTitleList = mutableListOf<String>()

    override fun getItem(position: Int): Fragment {
        /*return if (source == Source.CUSTOM)
            CustomFragment()
        else {*/
            return fragmentList[position]
        //}
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList.get(position)
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)

    }
}
