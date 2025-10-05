package com.example.lostnfound.repository

import androidx.lifecycle.LiveData
import com.example.lostnfound.dao.FoundItemDao
import com.example.lostnfound.entity.FoundItemEntity

class FoundItemRepository(private val foundItemDao: FoundItemDao) {
    val allItems: LiveData<List<FoundItemEntity>> = foundItemDao.getAllItems()

    suspend fun insert(item: FoundItemEntity): Long {
        return foundItemDao.insert(item)
    }

    suspend fun update(item: FoundItemEntity) {
        foundItemDao.update(item)
    }

    suspend fun delete(item: FoundItemEntity) {
        foundItemDao.delete(item)
    }

    fun getItemByStatus(status: String): LiveData<List<FoundItemEntity>> {
        return foundItemDao.getItemByStatus(status)
    }

    suspend fun getItemById(id: Int): FoundItemEntity? {
        return foundItemDao.getItemById(id)
    }

    fun searchItems(query: String): LiveData<List<FoundItemEntity>> {
        return foundItemDao.searchITEMS(query)
    }

    suspend fun deleteAll() {
        foundItemDao.deleteAll()
    }
}