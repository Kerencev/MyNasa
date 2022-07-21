package com.kerencev.mynasa.ui.recycler

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemTouchHelperCallBack(private val callBack: ItemTouchHelperAdapter) :
    ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipe = ItemTouchHelper.END or ItemTouchHelper.START
        val drag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(drag, swipe)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        callBack.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        callBack.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        when (viewHolder) {
            is RecyclerAdapter.MarsViewHolder -> {
                viewHolder.onItemSelected()
            }
            is RecyclerAdapter.EarthViewHolder -> {
                viewHolder.onItemSelected()
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        when (viewHolder) {
            is RecyclerAdapter.MarsViewHolder -> {
                viewHolder.onItemCleared()
            }
            is RecyclerAdapter.EarthViewHolder -> {
                viewHolder.onItemCleared()
            }
        }
    }
}