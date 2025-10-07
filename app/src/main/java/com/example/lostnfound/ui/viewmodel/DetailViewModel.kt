package com.example.lostnfound.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lostnfound.database.LostFoundDatabase
import com.example.lostnfound.database.User
import com.example.lostnfound.entity.FoundItemEntity
import com.example.lostnfound.entity.LostItemEntity
import com.example.lostnfound.repository.FoundItemRepository
import com.example.lostnfound.repository.LostItemRepository
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val foundRepo: FoundItemRepository
    private val lostRepo: LostItemRepository
    private val userDao = LostFoundDatabase.getDatabase(application).userDao()

    // 1. Siapkan "nampan" kosong (LiveData) untuk menaruh hasil data nanti.
    private val _itemDetail = MutableLiveData<Any?>()
    val itemDetail: LiveData<Any?> = _itemDetail

    private val _userData = MutableLiveData<User?>()
    val userData: LiveData<User?> = _userData

    init {
        val db = LostFoundDatabase.getDatabase(application)
        foundRepo = FoundItemRepository(db.foundItemDao())
        lostRepo = LostItemRepository(db.lostItemDao())
    }

    // 2. Buat fungsi untuk "memesan" data dari Fragment.
    fun loadItem(id: Int, type: String) {
        viewModelScope.launch {
            val item = if (type == "found") {
                foundRepo.getItemById(id)
            } else {
                lostRepo.getItemById(id)
            }
            _itemDetail.postValue(item)

            // Ambil data user berdasarkan userId dari item
            item?.let {
                val userEmail = when (it) {
                    is FoundItemEntity -> it.userEmail
                    is LostItemEntity -> it.userEmail
                    else -> null
                }

                userEmail?.let { email ->
                    loadUserData(email)
                }
            }
        }
    }

    private fun loadUserData(email: String) {
        viewModelScope.launch {
            val user = userDao.getUserByEmail(email) // Langsung pakai UserDao
            _userData.postValue(user)
        }
    }
}
