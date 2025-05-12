package com.ethiopianairlines.pilot.di

import com.ethiopianairlines.pilot.data.remote.api.ExamApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExamModule {
    @Provides
    @Singleton
    fun provideExamApi(retrofit: Retrofit): ExamApi {
        return retrofit.create(ExamApi::class.java)
    }
} 