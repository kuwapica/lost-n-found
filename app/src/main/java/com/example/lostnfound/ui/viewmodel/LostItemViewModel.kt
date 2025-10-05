package com.example.lostnfound.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lostnfound.data.model.FormStatus
import com.example.lostnfound.data.model.LostItem

class LostItemViewModel : ViewModel() {
    private val _formStatus = MutableLiveData<FormStatus>(FormStatus.Idle)
    val formStatus: LiveData<FormStatus> = _formStatus

    private val _selectedImage = MutableLiveData<Uri?>()
    val selectedImage: LiveData<Uri?> = _selectedImage

    private val _lostItems = MutableLiveData<List<LostItem>>(emptyList())
    val lostItems: LiveData<List<LostItem>> = _lostItems

    fun setSelectedImage(uri: Uri?) {
        _selectedImage.value = uri
    }

    fun submitLostItem(
        namaBarang: String,
        lokasiLost: String,
        waktu: String,
        deskripsi: String,
        imageUri: Uri?
    ) {
        _formStatus.value = FormStatus.Loading

        // Validasi
        when {
            imageUri == null -> {
                _formStatus.value = FormStatus.Error("Silakan upload foto")
                return
            }

            namaBarang.isBlank() -> {
                _formStatus.value = FormStatus.Error("Nama barang harus diisi")
                return
            }

            lokasiLost.isBlank() -> {
                _formStatus.value = FormStatus.Error("Lokasi harus diisi")
                return
            }

            waktu.isBlank() -> {
                _formStatus.value = FormStatus.Error("Waktu harus diisi")
                return
            }

            deskripsi.isBlank() -> {
                _formStatus.value = FormStatus.Error("Deskripsi harus diisi")
                return
            }
        }

        val lostItem = LostItem(
            id = System.currentTimeMillis().toString(),
            namaBarang = namaBarang,
            lokasiLost = lokasiLost,
            waktu = waktu,
            deskripsi = deskripsi,
            imageUri = imageUri
        )

        val currentList = _lostItems.value.orEmpty().toMutableList()
        currentList.add(lostItem)
        _lostItems.value = currentList

        _formStatus.value = FormStatus.Success("Data berhasil disimpan")
    }

    fun resetFormStatus() {
        _formStatus.value = FormStatus.Idle
    }

    fun clearFormData() {
        _selectedImage.value = null
        _formStatus.value = FormStatus.Idle
    }
}
