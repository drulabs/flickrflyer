package com.developerdru.flickflyers.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.developerdru.flickflyers.R
import com.developerdru.flickflyers.data.entities.Photo
import com.developerdru.flickflyers.utils.GlideApp
import com.developerdru.flickflyers.utils.generateImageURL

class HomePhotoAdapter(listener: Listener) : RecyclerView.Adapter<HomePhotoAdapter.PhotoVH>() {

    private var photoList: MutableList<Photo> = ArrayList()

    private val photoClickListener = listener

    fun resetPhotos(photos: List<Photo>) {
        photoList.clear()
        photoList.addAll(photos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoVH {
        val convertView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return PhotoVH(convertView)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: PhotoVH, position: Int) {
        holder.bind(position)
    }

    inner class PhotoVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgView = itemView.findViewById<ImageView>(R.id.img_photo_source)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_photo_title)

        fun bind(position: Int) {
            val photo = photoList[position]
            tvTitle.text = photo.title

            GlideApp.with(itemView)
                .load(photo.generateImageURL())
                .placeholder(R.drawable.loading_spinner)
                .error(R.drawable.ic_error_outline_24dp)
                .into(imgView)

            itemView.setOnClickListener {
                photoClickListener.onPhotoTapped(photo)
            }
        }
    }

    interface Listener {
        fun onPhotoTapped(photo: Photo)
    }
}