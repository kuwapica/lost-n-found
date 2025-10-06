package com.example.lostnfound.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lostnfound.dao.FoundItemDao
import com.example.lostnfound.dao.LostItemDao
import com.example.lostnfound.entity.FoundItemEntity
import com.example.lostnfound.entity.LostItemEntity

@Database(
    entities = [LostItemEntity::class, FoundItemEntity::class], //kalo ada entity lain tambahin aja
    version = 2,
    exportSchema = false
)
abstract class LostFoundDatabase : RoomDatabase() {
    abstract fun lostItemDao() : LostItemDao // ini juga tambahin aja
    abstract fun foundItemDao() : FoundItemDao

    companion object {
        @Volatile
        private var INSTANCE: LostFoundDatabase? = null

        fun getDatabase(context: Context): LostFoundDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LostFoundDatabase::class.java,
                    "lostfound_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}