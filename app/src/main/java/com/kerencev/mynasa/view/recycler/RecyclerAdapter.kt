package com.kerencev.mynasa.view.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kerencev.mynasa.databinding.RecyclerItemEarthBinding
import com.kerencev.mynasa.databinding.RecyclerItemHeaderBinding
import com.kerencev.mynasa.databinding.RecyclerItemMarsBinding

class RecyclerAdapter(private val listData: List<Data>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return listData[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EARTH -> {
                val binding = RecyclerItemEarthBinding.inflate(LayoutInflater.from(parent.context))
                EarthViewHolder(binding)
            }
            TYPE_MARS -> {
                val binding = RecyclerItemMarsBinding.inflate(LayoutInflater.from(parent.context))
                MarsViewHolder(binding)
            }
            else -> {
                val binding = RecyclerItemHeaderBinding.inflate(LayoutInflater.from(parent.context))
                HeaderViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class MarsViewHolder(val binding: RecyclerItemMarsBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    inner class EarthViewHolder(val binding: RecyclerItemEarthBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    inner class HeaderViewHolder(val binding: RecyclerItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}