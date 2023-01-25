package com.zeroillusion.binapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zeroillusion.binapp.data.BinItem

@Dao
interface BinDao {

    @Query("SELECT bin FROM bin_items")
    fun getBins(): Array<String>

    @Insert
    suspend fun insert(bin: BinItem)

    @Query("DELETE FROM bin_items")
    suspend fun deleteAll()
}