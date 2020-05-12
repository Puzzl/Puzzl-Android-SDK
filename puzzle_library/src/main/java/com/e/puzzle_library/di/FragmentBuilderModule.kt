package com.e.puzzle_library.di

import com.e.puzzle_library.createacc.CreateAccountFragment
import com.e.puzzle_library.onboarding.OnBoardingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface FragmentBuilderModule{
    @ContributesAndroidInjector
    @FragmentScope
    fun contributeOnBoardingFragment(): OnBoardingFragment
    @ContributesAndroidInjector
    @FragmentScope
    fun contributeCreateAccountFragment(): CreateAccountFragment
}