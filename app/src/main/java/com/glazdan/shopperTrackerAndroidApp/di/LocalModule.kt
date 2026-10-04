package com.glazdan.shopperTrackerAndroidApp.di

import android.content.Context
import androidx.room.Room
import com.glazdan.shopperTrackerAndroidApp.data.local.OfflineProductDao
import com.glazdan.shopperTrackerAndroidApp.data.local.OfflineProductsDatabase
import com.glazdan.shopperTrackerAndroidApp.data.repository.OfflineProductRepositoryImpl
import com.glazdan.shopperTrackerAndroidApp.domaim.repository.OfflineProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    @Provides
    @Singleton
    fun provideOfflineProduct(impl: OfflineProductRepositoryImpl): OfflineProductRepository = impl

    @Provides
    @Singleton
    fun provideDatabase( @ApplicationContext context: Context): OfflineProductsDatabase{
        return Room.databaseBuilder(
            context = context, klass = OfflineProductsDatabase::class.java, name = "products.db"
        ).fallbackToDestructiveMigration(dropAllTables = true).build()
    }

    @Provides
    fun provideDao( database: OfflineProductsDatabase): OfflineProductDao = database.productsDao()

}