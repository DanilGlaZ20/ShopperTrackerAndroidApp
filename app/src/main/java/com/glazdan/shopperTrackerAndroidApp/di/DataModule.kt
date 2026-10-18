package com.glazdan.shopperTrackerAndroidApp.di

import com.glazdan.shopperTrackerAndroidApp.data.repository.CategoryRepositoryImpl
import com.glazdan.shopperTrackerAndroidApp.data.repository.ProductRepositoryImpl
import com.glazdan.shopperTrackerAndroidApp.data.repository.UserRepositoryImpl
import com.glazdan.shopperTrackerAndroidApp.domaim.repository.CategoryRepository
import com.glazdan.shopperTrackerAndroidApp.domaim.repository.ProductRepository
import com.glazdan.shopperTrackerAndroidApp.domaim.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository = impl

    @Provides
    @Singleton
    fun provideCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository = impl

    @Provides
    @Singleton
    fun provideProductRepository(impl: ProductRepositoryImpl): ProductRepository = impl

}