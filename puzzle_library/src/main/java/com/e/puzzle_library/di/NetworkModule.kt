package com.library.di

import com.library.network.api.PuzzlApi
import com.library.network.repository.PuzzlRepository
import com.library.network.PuzzlService
import com.library.network.api.VeriffApi
import com.library.network.repository.VeriffRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun providePuzzlRepository(api : PuzzlApi) =
        PuzzlRepository(api)
    @Provides
    @Singleton
    fun providePuzzleService () = PuzzlService.createPuzzlService()
    @Provides
    @Singleton
    fun provideSessionService () = PuzzlService.createSessionService()
    @Provides
    @Singleton
    fun provideSessionRepository (api : VeriffApi) = VeriffRepository(api)
}