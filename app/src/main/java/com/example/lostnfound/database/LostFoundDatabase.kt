package com.example.lostnfound.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// nanti diuncomment terus yg lostitementity dihapus aja diganti punyamu
//@Database(
//    entities = [LostItemEntity::class], //kalo ada entity lain tambahin aja
//    version = 1,
//    exportSchema = false
//)
abstract class LostFoundDatabase : RoomDatabase() {
    // ini juga diuncomment terus ganti punyamu
//    abstract fun lostItemDao() : LostItemDao // ini juga tambahin aja

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