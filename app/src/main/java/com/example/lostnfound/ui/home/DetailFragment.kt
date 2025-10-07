package com.example.lostnfound.ui.home

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.navigateUp
import com.bumptech.glide.Glide
import com.example.lostnfound.databinding.FragmentDetailBinding
import com.example.lostnfound.entity.FoundItemEntity
import com.example.lostnfound.entity.LostItemEntity
import com.example.lostnfound.ui.viewmodel.DetailViewModel
import com.example.lostnfound.ui.home.DetailFragmentArgs
import java.text.SimpleDateFormat
import java.util.Locale

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp() // Fungsi untuk kembali ke layar sebelumnya
        }

        // 1. Perintahkan ViewModel untuk mengambil data (hanya panggil sekali)
        viewModel.loadItem(args.itemId, args.itemType)

        // 2. Amati (observe) LiveData 'itemDetail' untuk mendapatkan hasilnya
        viewModel.itemDetail.observe(viewLifecycleOwner) { item ->
            if (item != null) {
                // Tampilkan data ke UI
                when (item) {
                    is FoundItemEntity -> bindFoundItem(item)
                    is LostItemEntity -> bindLostItem(item)
                }
            }
        }
        viewModel.userData.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.tvUserName.text = user.nama
            } else {
                binding.tvUserName.text = "Pengguna Tidak Diketahui"
            }
        }

    }
    private fun bindFoundItem(item: FoundItemEntity) {
        binding.tvDetailNamaBarang.text = item.namaBarang
        binding.tvDetailLokasi.text = item.lokasiFound
        binding.tvDetailWaktu.text = formatTanggalLengkap(item.waktu)
        binding.tvDetailDeskripsi.text = item.deskripsi

        item.imagePath?.let {
            Glide.with(this).load(Uri.parse(it)).into(binding.ivDetailFoto)
        }
    }

    private fun bindLostItem(item: LostItemEntity) {
        binding.tvDetailNamaBarang.text = item.namaBarang
        binding.tvDetailLokasi.text = item.lokasiLost
        binding.tvDetailWaktu.text = formatTanggalLengkap(item.waktu)
        binding.tvDetailDeskripsi.text = item.deskripsi

        item.imagePath?.let {
            Glide.with(this).load(Uri.parse(it)).into(binding.ivDetailFoto)
        }
    }

    private fun formatTanggalLengkap(inputTanggal: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
            val date = inputFormat.parse(inputTanggal)
            outputFormat.format(date)
        } catch (e: Exception) {
            inputTanggal
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}