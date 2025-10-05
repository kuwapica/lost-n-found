package com.example.lostnfound.ui.addpost

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lostnfound.R
import com.example.lostnfound.databinding.FragmentAddBinding

class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // click listener untuk tombol lost
        binding.buttonLost.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_formLostFragment)
        }

        // click listener untuk tombol found
        binding.buttonFound.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_formFoundFragment)
        }
    }
}