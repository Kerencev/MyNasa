package com.kerencev.mynasa.ui.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kerencev.mynasa.R
import com.kerencev.mynasa.databinding.RecyclerItemEarthBinding
import com.kerencev.mynasa.databinding.RecyclerItemHeaderBinding
import com.kerencev.mynasa.databinding.RecyclerItemMarsBinding
import com.kerencev.mynasa.ui.recycler.diffutil.Change
import com.kerencev.mynasa.ui.recycler.diffutil.DiffUtilCallback
import com.kerencev.mynasa.ui.recycler.diffutil.createCombinePayload

fun interface AddItem {
    fun add(position: Int, type: Int)
}

fun interface RemoveItem {
    fun remove(position: Int)
}

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemDismiss(position: Int)
}

interface ItemTouchHelperViewHolder {
    fun onItemSelected()
    fun onItemCleared()
}

class RecyclerAdapter(
    private var listData: MutableList<Pair<Data, Boolean>>,
    private val callBackAdd: AddItem,
    private val callBackRemove: RemoveItem
) :
    RecyclerView.Adapter<RecyclerAdapter.BaseViewHolder>(), ItemTouchHelperAdapter {

    fun setListDataForDiffUtil(lisDataNew: MutableList<Pair<Data, Boolean>>) {
        val diff = DiffUtil.calculateDiff(DiffUtilCallback(listData, lisDataNew))
        diff.dispatchUpdatesTo(this)
        listData = lisDataNew
    }

    fun setListDataAdd(listDataNew: MutableList<Pair<Data, Boolean>>, position: Int) {
        listData = listDataNew
        notifyItemInserted(position)
    }

    fun setListDataRemove(listDataNew: MutableList<Pair<Data, Boolean>>, position: Int) {
        listData = listDataNew
        notifyItemRemoved(position)
    }

    override fun getItemViewType(position: Int): Int {
        return listData[position].first.type
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

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val createCombinePayload = createCombinePayload(payloads as List<Change<Pair<Data, Boolean>>>)
            if (createCombinePayload.newData.first.name != createCombinePayload.oldData.first.name) {
                holder.itemView.findViewById<TextView>(R.id.name).text = createCombinePayload.newData.first.name
            }
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    abstract class BaseViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        abstract fun bind(data: Pair<Data, Boolean>)
    }

    inner class MarsViewHolder(val binding: RecyclerItemMarsBinding) :
        BaseViewHolder(binding.root), ItemTouchHelperViewHolder {
        override fun bind(data: Pair<Data, Boolean>) = with(binding) {
            name.text = data.first.name
            addItemImageView.setOnClickListener {
                callBackAdd.add(layoutPosition, TYPE_MARS)
            }
            removeItemImageView.setOnClickListener {
                callBackRemove.remove(layoutPosition)
            }
            moveItemUp.setOnClickListener {
                if (layoutPosition == 1) return@setOnClickListener
                listData.removeAt(layoutPosition).apply {
                    listData.add(layoutPosition - 1, this)
                }
                notifyItemMoved(layoutPosition, layoutPosition - 1)
            }
            moveItemDown.setOnClickListener {
                if (layoutPosition == listData.size - 1) return@setOnClickListener
                listData.removeAt(layoutPosition).apply {
                    listData.add(layoutPosition + 1, this)
                }
                notifyItemMoved(layoutPosition, layoutPosition + 1)
            }
            marsDescriptionTextView.visibility =
                if (listData[layoutPosition].second) View.VISIBLE else View.GONE
            marsImageView.setOnClickListener {
                listData[layoutPosition] = listData[layoutPosition].let {
                    it.first to !it.second
                }
                notifyItemChanged(layoutPosition)
            }
        }

        override fun onItemSelected() {
            binding.root.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.purple_700))
        }

        override fun onItemCleared() {
            binding.root.setBackgroundColor(0)
        }
    }

    inner class EarthViewHolder(val binding: RecyclerItemEarthBinding) :
        BaseViewHolder(binding.root), ItemTouchHelperViewHolder {
        override fun bind(data: Pair<Data, Boolean>) = with(binding) {
            name.text = data.first.name
            addItemImageView.setOnClickListener {
                callBackAdd.add(layoutPosition, TYPE_EARTH)
            }
            removeItemImageView.setOnClickListener {
                callBackRemove.remove(layoutPosition)
            }
            moveItemUp.setOnClickListener {
                if (layoutPosition == 1) return@setOnClickListener
                listData.removeAt(layoutPosition).apply {
                    listData.add(layoutPosition - 1, this)
                }
                notifyItemMoved(layoutPosition, layoutPosition - 1)
            }
            moveItemDown.setOnClickListener {
                if (layoutPosition == listData.size - 1) return@setOnClickListener
                listData.removeAt(layoutPosition).apply {
                    listData.add(layoutPosition + 1, this)
                }
                notifyItemMoved(layoutPosition, layoutPosition + 1)
            }
        }

        override fun onItemSelected() {
            binding.root.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.purple_700))
        }

        override fun onItemCleared() {
            binding.root.setBackgroundColor(0)
        }
    }

    inner class HeaderViewHolder(val binding: RecyclerItemHeaderBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(data: Pair<Data, Boolean>) {
            binding.name.text = data.first.name
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        listData.removeAt(fromPosition).apply {
            listData.add(toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        callBackRemove.remove(position)
    }
}