package com.example.lostnfound.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lostnfound.database.LostFoundDatabase
import com.example.lostnfound.repository.FoundItemRepository
import com.example.lostnfound.repository.LostItemRepository
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val foundRepo: FoundItemRepository
    private val lostRepo: LostItemRepository

    // 1. Siapkan "nampan" kosong (LiveData) untuk menaruh hasil data nanti.
    private val _itemDetail = MutableLiveData<Any?>()
    val itemDetail: LiveData<Any?> = _itemDetail

    init {
        val db = LostFoundDatabase.getDatabase(application)
        foundRepo = FoundItemRepository(db.foundItemDao())
        lostRepo = LostItemRepository(db.lostItemDao())
    }

    // 2. Buat fungsi untuk "memesan" data dari Fragment.
    fun loadItem(id: Int, type: String) {
        // 3. Jalankan pesanan di "dapur" (latar belakang) menggunakan viewModelScope.
        viewModelScope.launch {
            // Panggil perintah 'suspend' dari Repository (aman di sini).
            val item = if (type == "found") {
                foundRepo.getItemById(id)
            } else {
                lostRepo.getItemById(id)
            }
            // 4. Setelah data "matang", taruh di "nampan" (LiveData).
            _itemDetail.postValue(item)
        }
    }
}