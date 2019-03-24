package com.developerdru.flickflyers.ui.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.developerdru.flickflyers.R
import com.developerdru.flickflyers.data.entities.Photo
import com.developerdru.flickflyers.utils.GlideApp
import com.developerdru.flickflyers.utils.fromJSON
import com.developerdru.flickflyers.utils.generateImageURL
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val KEY_PHOTO_ENTITY = "photo_entity"
    }

    private var currentPhoto: Photo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        intent.extras?.let {
            val photoRep: String? = it.getString(KEY_PHOTO_ENTITY)
            if (photoRep != null) {
                currentPhoto = Photo.fromJSON(photoRep)
            }
        }

        currentPhoto?.let {
            GlideApp.with(this@DetailsActivity)
                .load(it.generateImageURL())
                .placeholder(R.drawable.loading_spinner)
                .error(R.drawable.ic_error_outline_24dp)
                .into(imgPhotoDetails)

            tvDetailsTitle.text = it.title
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                this@DetailsActivity.finish()
            }
        }
        return false
    }
}
