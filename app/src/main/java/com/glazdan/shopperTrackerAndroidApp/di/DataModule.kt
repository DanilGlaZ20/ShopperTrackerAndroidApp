package com.glazdan.shopperTrackerAndroidApp.di

import com.glazdan.shopperTrackerAndroidApp.data.repository.UserRepositoryImpl
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

}