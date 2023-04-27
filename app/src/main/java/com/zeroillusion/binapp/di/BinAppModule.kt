package com.zeroillusion.binapp.di

import android.app.Application
import androidx.room.Room
import com.zeroillusion.binapp.data.local.BinDatabase
import com.zeroillusion.binapp.data.remote.BinApi
import com.zeroillusion.binapp.data.repository.BinAppRepositoryImpl
import com.zeroillusion.binapp.domain.repository.BinAppRepository
import com.zeroillusion.binapp.domain.use_case.DeleteBinHistory
import com.zeroillusion.binapp.domain.use_case.GetBinFromApi
import com.zeroillusion.binapp.domain.use_case.GetBinFromDatabase
import com.zeroillusion.binapp.domain.use_case.GetBinList
import com.zeroillusion.binapp.domain.use_case.ValidateBin
import com.zeroillusion.binapp.domain.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BinAppModule {

    @Provides
    @Singleton
    fun provideValidateBin(): ValidateBin {
        return ValidateBin()
    }

    @Provides
    @Singleton
    fun provideGetBinFromApi(repository: BinAppRepository): GetBinFromApi {
        return GetBinFromApi(repository)
    }

    @Provides
    @Singleton
    fun provideGetBinFromDatabase(repository: BinAppRepository): GetBinFromDatabase {
        return GetBinFromDatabase(repository)
    }

    @Provides
    @Singleton
    fun provideGetBinList(repository: BinAppRepository): GetBinList {
        return GetBinList(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteBinHistory(repository: BinAppRepository): DeleteBinHistory {
        return DeleteBinHistory(repository)
    }

    @Provides
    @Singleton
    fun provideBinAppRepository(
        db: BinDatabase,
        api: BinApi
    ): BinAppRepository {
        return BinAppRepositoryImpl(api, db.dao)
    }

    @Provides
    @Singleton
    fun provideBinDatabase(app: Application): BinDatabase {
        return Room.databaseBuilder(
            app, BinDatabase::class.java, "bin_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBinApi(): BinApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BinApi::class.java)
    }
}