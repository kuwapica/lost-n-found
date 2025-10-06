package com.example.lostnfound

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lostnfound.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back -> pergi ke profile
        binding.btnBack.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.nav_profile)
            }, 500)
        }

        binding.layoutNama.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_editProfile_to_editNama)
            }, 500)
        }

        binding.layoutPassword.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_editProfile_to_editPassword)
            }, 500)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
