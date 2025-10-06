package com.example.lostnfound.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lostnfound.data.model.FormStatus
import com.example.lostnfound.database.LostFoundDatabase
import com.example.lostnfound.entity.FoundItemEntity
import com.example.lostnfound.repository.FoundItemRepository
import kotlinx.coroutines.launch

class FoundItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FoundItemRepository
    val allItems: LiveData<List<FoundItemEntity>>

    init {
        val foundItemDao = LostFoundDatabase.getDatabase(application).foundItemDao()
        repository = FoundItemRepository(foundItemDao)
        allItems = repository.allItems
    }
    private val _formStatus = MutableLiveData<FormStatus>(FormStatus.Idle)
    val formStatus: LiveData<FormStatus> = _formStatus

    private val _selectedImage = MutableLiveData<Uri?>()
    val selectedImage: LiveData<Uri?> = _selectedImage

    fun setSelectedImage(uri: Uri?) {
        _selectedImage.value = uri
    }

    fun submitFoundItem(
        namaBarang: String,
        lokasiFound: String,
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

            lokasiFound.isBlank() -> {
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
                val foundItem = FoundItemEntity(
                    namaBarang = namaBarang,
                    lokasiFound = lokasiFound,
                    waktu = waktu,
                    deskripsi = deskripsi,
                    imagePath = imageUri.toString(),
                    status = "found"
                )

                repository.insert(foundItem)

                _formStatus.value = FormStatus.Success("Data berhasil disimpan")
            } catch (e: Exception) {
                _formStatus.value = FormStatus.Error("Gagal menyimpan data: ${e.message}")
            }
        }
    }

    fun getItemByStatus(status: String): LiveData<List<FoundItemEntity>> {
        return repository.getItemByStatus(status)
    }

    fun searchItems(query: String): LiveData<List<FoundItemEntity>> {
        return repository.searchItems(query)
    }

    fun deleteItem(item: FoundItemEntity) {
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