package com.example.lostnfound.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lostnfound.adapter.LostItemAdapter
import com.example.lostnfound.databinding.FragmentListBinding
import com.example.lostnfound.ui.viewmodel.LostItemViewModel
import androidx.navigation.fragment.findNavController
import com.example.lostnfound.ui.home.HomeFragmentDirections

class LostListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    // Inisialisasi ViewModel yang sesuai
    private val viewModel: LostItemViewModel by viewModels()

    // Deklarasikan adapter yang sesuai
    private lateinit var lostItemAdapter: LostItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeLostItems()
    }

    private fun setupRecyclerView() {
        lostItemAdapter = LostItemAdapter(emptyList()) { item ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                itemId = item.id,
                itemType = "lost"
            )
            findNavController().navigate(action)
        }
        binding.recyclerViewList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = lostItemAdapter
        }
    }

    private fun observeLostItems() {
        viewModel.allItems.observe(viewLifecycleOwner) { items ->
            lostItemAdapter.updateData(items) // Panggil updateData di adapter yang benar

            if (items.isEmpty()) {
                binding.recyclerViewList.visibility = View.GONE
            } else {
                binding.recyclerViewList.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}