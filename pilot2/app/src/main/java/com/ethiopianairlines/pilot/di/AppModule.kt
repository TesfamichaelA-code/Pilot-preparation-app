package com.ethiopianairlines.pilot.di

import android.app.Application
import com.ethiopianairlines.pilot.data.remote.api.AuthApi
import com.ethiopianairlines.pilot.data.repository.AuthRepositoryImpl
import com.ethiopianairlines.pilot.domain.repository.AuthRepository
import com.ethiopianairlines.pilot.domain.usecase.LoginUserUseCase
import com.ethiopianairlines.pilot.domain.usecase.RegisterUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi = Retrofit.Builder()
        .baseUrl("http://192.168.191.135:5000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi): AuthRepository = AuthRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideRegisterUserUseCase(repository: AuthRepository): RegisterUserUseCase = RegisterUserUseCase(repository)

    @Provides
    @Singleton
    fun provideLoginUserUseCase(repository: AuthRepository): LoginUserUseCase = LoginUserUseCase(repository)
} 