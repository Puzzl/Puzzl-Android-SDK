package com.library.di

import android.content.Context
import com.e.puzzle_library.PuzzleApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule (private val application : PuzzleApplication){
    @Provides
    @Singleton
    fun provideApplication() = application

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application.applicationContext

}