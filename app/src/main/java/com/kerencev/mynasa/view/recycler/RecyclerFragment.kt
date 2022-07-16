package com.kerencev.mynasa.view.recycler

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
        Pair(Data("Заголовок", type = TYPE_HEADER), false),
        Pair(Data("Марс", type = TYPE_MARS), false),
        Pair(Data("Земля", type = TYPE_EARTH), false),
        Pair(Data("Земля", type = TYPE_EARTH), false),
        Pair(Data("Марс", type = TYPE_MARS), false),
        Pair(Data("Марс", type = TYPE_MARS), false),
        Pair(Data("Земля", type = TYPE_EARTH), false),
        Pair(Data("Земля", type = TYPE_EARTH), false)
    )
    private lateinit var adapter: RecyclerAdapter
    private val callBackAdd = AddItem { position, type ->
        when (type) {
            TYPE_EARTH -> {
                data.add(position, Pair(Data("Earth", type = TYPE_EARTH), false))
            }
            TYPE_MARS -> {
                data.add(position, Pair(Data("Mars", type = TYPE_MARS), false))
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}