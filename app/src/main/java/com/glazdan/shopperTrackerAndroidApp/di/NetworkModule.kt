package com.glazdan.shopperTrackerAndroidApp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.json

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient{
        return HttpClient(CIO){
            install(ContentNegotiation) {
                json(Json{
                    ignoreUnknownKeys = true
                    isLenient = true
                })

            }

            defaultRequest{
                url{
                    protocol = URLProtocol.HTTP
                    host = "192.168.1.5"
                    port = 8080
                    //path("api/v1/")

                }
            }
        }

    }

}

