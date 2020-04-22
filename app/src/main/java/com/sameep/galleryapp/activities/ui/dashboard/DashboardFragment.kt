package com.sameep.galleryapp.activities.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.kogicodes.sokoni.models.custom.Status
import com.sameep.galleryapp.R
import com.sameep.galleryapp.singletons.InstanceProvider
import ke.co.calista.googlephotos.models.MediaItemObj


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var token : String?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val pref = requireContext().getSharedPreferences("gToken", Context.MODE_PRIVATE)
        token = pref.getString("gToken", null)

        Log.e("PrefToken>> ", "$token <<<")

        dashboardViewModel = DashboardViewModel()
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = requireActivity().getSharedPreferences("serverCode", Context.MODE_PRIVATE).getString("serverCode", "")
        token?.let {
            dashboardViewModel.getMediaItem(it)
        }

        /*dashboardViewModel.observeMediaItem().observe(requireActivity(), Observer {
            if(it.status == Status.SUCCESS){
                for (m in it.data!!){
                    mediaItems?.add(MediaItemObj(m.baseUrl+ "=w480-h480",false))

                }
            }
        })
        )*/

    }

}
