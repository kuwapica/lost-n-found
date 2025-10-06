package com.example.lostnfound.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lostnfound.adapter.FoundItemAdapter
import com.example.lostnfound.databinding.FragmentListBinding // Gunakan ViewBinding
import com.example.lostnfound.ui.viewmodel.FoundItemViewModel

class FoundListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    // Inisialisasi ViewModel
    private val viewModel: FoundItemViewModel by viewModels()

    // Deklarasikan adapter
    private lateinit var foundItemAdapter: FoundItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Setup Adapter dan RecyclerView
        setupRecyclerView()

        // 2. Amati (observe) perubahan data dari ViewModel
        observeFoundItems()
    }

    private fun setupRecyclerView() {
        foundItemAdapter = FoundItemAdapter(emptyList()) { item ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                itemId = item.id,
                itemType = "found"
            )
            findNavController().navigate(action)
        }
        binding.recyclerViewList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = foundItemAdapter
        }
    }

    private fun observeFoundItems() {
        // viewModel.allItems adalah LiveData<List<FoundItemEntity>>
        viewModel.allItems.observe(viewLifecycleOwner) { items ->
            // Ketika data berubah (misal: ada item baru disubmit),
            // update data di dalam adapter.
            foundItemAdapter.updateData(items)

            // Opsional: Tampilkan pesan jika list kosong
            if (items.isEmpty()) {
                binding.recyclerViewList.visibility = View.GONE
                // Pastikan Anda punya TextView di layout untuk pesan ini
                // binding.tvEmptyList.visibility = View.VISIBLE
            } else {
                binding.recyclerViewList.visibility = View.VISIBLE
                // binding.tvEmptyList.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}