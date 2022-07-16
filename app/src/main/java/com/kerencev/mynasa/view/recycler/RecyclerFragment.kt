package com.kerencev.mynasa.view.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kerencev.mynasa.databinding.FragmentRecyclerBinding

class RecyclerFragment : Fragment() {
    private var _binding: FragmentRecyclerBinding? = null
    private val binding get() = _binding!!
    private val data = arrayListOf(
        Data("Заголовок", type = TYPE_HEADER),
        Data("Earth", type = TYPE_EARTH),
        Data("Earth", type = TYPE_EARTH),
        Data("Mars", type = TYPE_MARS),
        Data("Earth", type = TYPE_EARTH),
        Data("Earth", type = TYPE_EARTH),
        Data("Earth", type = TYPE_EARTH),
        Data("Mars", type = TYPE_MARS)
    )
    private lateinit var adapter: RecyclerAdapter
    private val callBackAdd = AddItem { position, type ->
        when (type) {
            TYPE_EARTH -> {
                data.add(position, Data("Earth", type = TYPE_EARTH))
            }
            TYPE_MARS -> {
                data.add(position, Data("Mars", type = TYPE_MARS))
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}