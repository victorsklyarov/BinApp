package com.zeroillusion.binapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zeroillusion.binapp.data.local.entity.BinEntity

@Database(
    entities = [BinEntity::class],
    version = 1
)
abstract class BinDatabase : RoomDatabase() {

    abstract val dao: BinDao
}