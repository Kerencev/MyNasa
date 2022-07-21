package com.kerencev.mynasa.ui.mars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kerencev.mynasa.R
import com.kerencev.mynasa.data.retrofit.entities.mars.roverphotos.Photo

fun interface OnPhotoClick {
    fun onClick(imageUrl: String)
}

class MarsPhotoAdapter(private val onPhotoClick: OnPhotoClick) :
    RecyclerView.Adapter<MarsPhotoAdapter.MarsPhotoViewHolder>() {

    private val data: ArrayList<Photo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsPhotoViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.mars_photo_item, parent, false)
        return MarsPhotoViewHolder(itemView = itemView)
    }

    override fun onBindViewHolder(holder: MarsPhotoViewHolder, position: Int) {
        holder.image.load(data[position].img_src) {
            placeholder(R.drawable.bg_mars)
        }
        holder.image.setOnClickListener {
            onPhotoClick.onClick(data[position].img_src)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(dat: List<Photo>) {
        data.addAll(dat)
        notifyDataSetChanged()
    }

    inner class MarsPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.img_mars_photo)
    }
}