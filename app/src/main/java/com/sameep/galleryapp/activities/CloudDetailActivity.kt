package com.sameep.galleryapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sameep.galleryapp.dataclasses.PhotoModel
import com.sameep.galleryapp.R
import com.sameep.galleryapp.singletons.GlideProvider
import kotlinx.android.synthetic.main.activity_cloud_detail.*

class CloudDetailActivity : AppCompatActivity() {

    companion object{
        val CLOUD_DATA="CLOUD_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud_detail)

        val data = intent.extras.getParcelable<PhotoModel>(CLOUD_DATA)

        cloud_tv_title.text=data.title
        val url =  "https://farm${data
            .farm}.staticflickr.com/${data.server}/${data.id}_${data.secret}_z.jpg"
        GlideProvider.getGlide(this).load(url).into(cloud_iv)
        Log.e("Desc>>", data.title+"<<<")

    }
}
