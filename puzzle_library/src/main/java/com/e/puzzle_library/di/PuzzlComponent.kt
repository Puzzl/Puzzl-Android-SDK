package com.library.di

import com.e.puzzle_library.PuzzleApplication
import com.library.network.repository.PuzzlRepository
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(modules = [AppModule::class,
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    ActivitiesBuilderModule::class,
    NetworkModule::class])
@Singleton
interface AppComponent {
    fun inject(yoogenApp: PuzzleApplication)
    fun getPuzzleRepository(): PuzzlRepository
}