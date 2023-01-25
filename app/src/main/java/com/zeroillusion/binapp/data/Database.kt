package com.zeroillusion.binapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zeroillusion.binapp.dao.BinDao

@Database(entities = [BinItem::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun binDao(): BinDao
}