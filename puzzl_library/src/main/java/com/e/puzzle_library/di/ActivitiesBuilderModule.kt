package com.library.di

import com.e.puzzl_library.VeriffActivity
import com.e.puzzl_library.di.FragmentBuilderModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivitiesBuilderModule {
    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    fun contributeVeriffActivity() : VeriffActivity
}