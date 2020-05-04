package com.sameep.galleryapp.activities

import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.sameep.galleryapp.R
import com.sameep.galleryapp.dataclasses.Media
import com.sameep.galleryapp.singletons.GlideProvider
import kotlinx.android.synthetic.main.activity_image_detail.*
import java.text.SimpleDateFormat
import java.util.*

class ImageDetailActivity : AppCompatActivity() {

    companion object {
        val INTENT_DATA = "INTENT_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)
        val bundle = intent.extras
        val extra_data = bundle?.getParcelable<Media>(INTENT_DATA)

        extra_data?.let {
            GlideProvider.getGlide(this)
                .load(extra_data.thumbnailUrl)
                .into(detail_iv)
            extra_data.date?.let {
                detail_tv_date.text = getDate(extra_data.date.toLong(), "MMM dd, yyyy hh:mm:ss.SSS")
            }
            extra_data.mime?.let {
                detail_tv_name.text =it
            }

            extra_data.name?.let {
                detail_tv_name.text=it
            }

            val type = extra_data.type
            if (type != 0) {
                if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
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
