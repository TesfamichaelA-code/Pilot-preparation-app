package com.ethiopianairlines.pilot.di

import com.ethiopianairlines.pilot.data.remote.api.InterviewApi
import com.ethiopianairlines.pilot.data.repository.InterviewRepository
import com.ethiopianairlines.pilot.data.repository.InterviewRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InterviewModule {
    @Provides
    @Singleton
    fun provideInterviewApi(retrofit: Retrofit): InterviewApi =
        retrofit.create(InterviewApi::class.java)

    @Provides
    @Singleton
    fun provideInterviewRepository(api: InterviewApi): InterviewRepository =
        InterviewRepositoryImpl(api)
} 