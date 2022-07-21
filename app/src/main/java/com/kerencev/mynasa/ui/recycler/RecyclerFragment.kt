package com.kerencev.mynasa.ui.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.kerencev.mynasa.databinding.FragmentRecyclerBinding

class RecyclerFragment : Fragment() {
    private var _binding: FragmentRecyclerBinding? = null
    private val binding get() = _binding!!
    private val data = arrayListOf(
        Pair(Data(id = 0, name = "Заголовок", type = TYPE_HEADER), false),
        Pair(Data(id = 1, name = "Земля", type = TYPE_EARTH), false),
        Pair(Data(id = 2, name = "Земля", type = TYPE_EARTH), false),
        Pair(Data(id = 3, name = "Марс", type = TYPE_MARS), false),
        Pair(Data(id = 4, name = "Земля", type = TYPE_EARTH), false),
        Pair(Data(id = 5, name = "Марс", type = TYPE_MARS), false),
        Pair(Data(id = 6, name = "Марс", type = TYPE_MARS), false),
        Pair(Data(id = 7, name = "Земля", type = TYPE_EARTH), false)
    )
    private var isNewList = false
    private lateinit var adapter: RecyclerAdapter
    private val callBackAdd = AddItem { position, type ->
        when (type) {
            TYPE_EARTH -> {
                data.add(position, Pair(Data(id = 0, "Earth", type = TYPE_EARTH), false))
            }
            TYPE_MARS -> {
                data.add(position, Pair(Data(id = 0, "Mars", type = TYPE_MARS), false))
            }
        }
        adapter.setListDataAdd(data, position)
    }
    private val callBackRemove = RemoveItem {
        data.removeAt(it)
        adapter.setListDataRemove(data, it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RecyclerAdapter(data, callBackAdd, callBackRemove)
        binding.recyclerView.adapter = adapter
        ItemTouchHelper(ItemTouchHelperCallBack(adapter)).attachToRecyclerView(binding.recyclerView)
        binding.fab.setOnClickListener {
            changeAdapterData()
        }
    }

    private fun changeAdapterData() {
        adapter.setListDataForDiffUtil(createItemList(isNewList).map { it }.toMutableList())
        isNewList = !isNewList
    }

    private fun createItemList(instanceNumber: Boolean): List<Pair<Data, Boolean>> {
        return when (instanceNumber) {
            false -> listOf(
                Pair(Data(0, "Header", type = TYPE_HEADER), false),
                Pair(Data(1, "Mars", ""), false),
                Pair(Data(2, "Mars", ""), false),
                Pair(Data(3, "Mars", ""), false),
                Pair(Data(4, "Mars", ""), false),
                Pair(Data(5, "Mars", ""), false),
                Pair(Data(6, "Mars", ""), false)
            )
            true -> listOf(
                Pair(Data(0, "Header",type = TYPE_HEADER), false),
                Pair(Data(1, "Mars", ""), false),
                Pair(Data(2, "Jupiter", ""), false),
                Pair(Data(3, "Mars", ""), false),
                Pair(Data(4, "Neptune", ""), false),
                Pair(Data(5, "Saturn", ""), false),
                Pair(Data(6, "Mars", ""), false)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}