package com.sameep.galleryapp.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sameep.galleryapp.R
import com.sameep.galleryapp.dataclasses.PictureFacer
import kotlinx.android.synthetic.main.activity_image_detail.*
import java.text.SimpleDateFormat
import java.util.*

class ImageDetailActivity : AppCompatActivity() {

    object dataKey {
        val INTENT_DATA = "INTENT_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)
        val extra_data = intent.getParcelableExtra<PictureFacer>(dataKey.INTENT_DATA)

        extra_data.let {
            detail_iv.setImageURI(Uri.parse(extra_data.picturePath))
            detail_tv_date.text = getDate(extra_data.date.toLong(), "MMM dd, yyyy hh:mm:ss.SSS")
            detail_tv_mime.text = (extra_data.mime)
            detail_tv_name.text = extra_data.picturName
            val type = extra_data.type
            if (type != 0) {
                if (type == 1)
                    detail_tv_type.text = "Image"
                else
                    detail_tv_type.text = "Video"
            }

        }

    }

    fun getDate(milliSeconds: Long, dateFormat: String): String {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
