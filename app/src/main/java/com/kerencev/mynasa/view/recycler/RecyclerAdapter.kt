package com.kerencev.mynasa.view.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kerencev.mynasa.databinding.RecyclerItemEarthBinding
import com.kerencev.mynasa.databinding.RecyclerItemHeaderBinding
import com.kerencev.mynasa.databinding.RecyclerItemMarsBinding

fun interface AddItem {
    fun add(position: Int, type: Int)
}

fun interface RemoveItem {
    fun remove(position: Int)
}

class RecyclerAdapter(
    private var listData: List<Data>,
    private val callBackAdd: AddItem,
    private val callBackRemove: RemoveItem
) :
    RecyclerView.Adapter<RecyclerAdapter.BaseViewHolder>() {

    fun setListDataAdd(listDataNew: List<Data>, position: Int) {
        listData = listDataNew
        notifyItemInserted(position)
    }

    fun setListDataRemove(listDataNew: List<Data>, position: Int) {
        listData = listDataNew
        notifyItemRemoved(position)
    }

    override fun getItemViewType(position: Int): Int {
        return listData[position].type
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapter.BaseViewHolder {
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

    override fun onBindViewHolder(holder: RecyclerAdapter.BaseViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    abstract class BaseViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        abstract fun bind(data: Data)
    }

    inner class MarsViewHolder(val binding: RecyclerItemMarsBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(data: Data) = with(binding) {
            name.text = data.name
            addItemImageView.setOnClickListener {
                callBackAdd.add(layoutPosition, TYPE_MARS)
            }
            removeItemImageView.setOnClickListener {
                callBackRemove.remove(layoutPosition)
            }
        }
    }

    inner class EarthViewHolder(val binding: RecyclerItemEarthBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(data: Data) = with(binding) {
            name.text = data.name
            addItemImageView.setOnClickListener {
                callBackAdd.add(layoutPosition, TYPE_EARTH)
            }
            removeItemImageView.setOnClickListener {
                callBackRemove.remove(layoutPosition)
            }
        }
    }

    inner class HeaderViewHolder(val binding: RecyclerItemHeaderBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(data: Data) {
            binding.name.text = data.name
        }
    }
}