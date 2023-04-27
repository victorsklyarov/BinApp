package com.zeroillusion.binapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zeroillusion.binapp.data.local.entity.BinEntity

@Dao
interface BinDao {

    @Insert(entity = BinEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBin(bin: BinEntity)

    @Query("SELECT * FROM binentity")
    suspend fun getBinList(): List<BinEntity>

    @Query("SELECT * FROM binentity WHERE bin IN (:bin) LIMIT 1")
    suspend fun getBin(bin: Int): BinEntity

    @Query("DELETE FROM binentity")
    suspend fun deleteAllBins()
}
