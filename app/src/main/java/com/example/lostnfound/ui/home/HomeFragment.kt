package com.example.lostnfound.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lostnfound.ui.adapter.SectionsPagerAdapter
import com.example.lostnfound.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Siapkan adapter untuk ViewPager
        val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity() as AppCompatActivity)

        // 2. Pasang adapter ke ViewPager
        binding.viewPager.adapter = sectionsPagerAdapter

        // 3. Hubungkan TabLayout dengan ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            // Menentukan judul untuk setiap tab
            tab.text = when (position) {
                0 -> "Lost"
                1 -> "Found"
                else -> ""
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}