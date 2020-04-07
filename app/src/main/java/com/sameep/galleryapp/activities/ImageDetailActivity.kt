package com.sameep.galleryapp.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sameep.galleryapp.R
import kotlinx.android.synthetic.main.activity_image_detail.*
import java.text.SimpleDateFormat
import java.util.*

class ImageDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)
        val bundle: Bundle? = intent.extras

        detail_iv.setImageURI(Uri.parse(bundle?.getString("path")))
        detail_tv_date.text = getDate(bundle!!.getLong("date",0), "MMM dd, yyyy hh:mm:ss.SSS")
        detail_tv_mime.text = (bundle?.getString("mime"))
        detail_tv_name.text = bundle?.getString("name")
        val type = bundle?.getInt("type",0)
        if (type != 0){
        if (type==1)
            detail_tv_type.text="Image"
        else
            detail_tv_type.text="Video"}

    }

    fun getDate(milliSeconds : Long,  dateFormat: String):String
    {
        // Create a DateFormatter object for displaying date in specified format.
         val formatter = SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
