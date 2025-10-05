package com.example.lostnfound.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lostnfound.data.model.FormStatus
import com.example.lostnfound.database.LostFoundDatabase
import com.example.lostnfound.entity.LostItemEntity
import com.example.lostnfound.repository.LostItemRepository
import kotlinx.coroutines.launch

class LostItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LostItemRepository
    val allItems: LiveData<List<LostItemEntity>>

    init {
        val lostItemDao = LostFoundDatabase.getDatabase(application).lostItemDao()
        repository = LostItemRepository(lostItemDao)
        allItems = repository.allItems
    }
    private val _formStatus = MutableLiveData<FormStatus>(FormStatus.Idle)
    val formStatus: LiveData<FormStatus> = _formStatus

    private val _selectedImage = MutableLiveData<Uri?>()
    val selectedImage: LiveData<Uri?> = _selectedImage

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

        viewModelScope.launch {
            try {
                val lostItem = LostItemEntity(
                    namaBarang = namaBarang,
                    lokasiLost = lokasiLost,
                    waktu = waktu,
                    deskripsi = deskripsi,
                    imagePath = imageUri.toString(),
                    status = "lost"
                )

                repository.insert(lostItem)

                _formStatus.value = FormStatus.Success("Data berhasil disimpan")
            } catch (e: Exception) {
                _formStatus.value = FormStatus.Error("Gagal menyimpan data: ${e.message}")
            }
        }
    }

    fun getItemByStatus(status: String): LiveData<List<LostItemEntity>> {
        return repository.getItemByStatus(status)
    }

    fun searchItems(query: String): LiveData<List<LostItemEntity>> {
        return repository.searchItems(query)
    }

    fun deleteItem(item: LostItemEntity) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }

    fun resetFormStatus() {
        _formStatus.value = FormStatus.Idle
    }

    fun clearFormData() {
        _selectedImage.value = null
        _formStatus.value = FormStatus.Idle
    }
}
