package com.sameep.galleryapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sameep.galleryapp.R
import com.sameep.galleryapp.viewmodel.LocalMediaViewModel


class LocalMediaFragment : Fragment() {

    companion object {
        fun newInstance() = LocalMediaFragment()
    }

    private lateinit var viewModel: LocalMediaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.local_media_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = LocalMediaViewModel(activity!!.application)
        // TODO: Use the ViewModel
    }

}
