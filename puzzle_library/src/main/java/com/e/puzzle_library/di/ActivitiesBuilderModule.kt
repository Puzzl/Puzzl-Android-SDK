package com.library.di

import com.library.createacc.CreateAccountActivity
import com.library.onboarding.OnBoardingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivitiesBuilderModule {
    @ContributesAndroidInjector
    fun contributeOnBoardingActivity() : OnBoardingActivity
    @ContributesAndroidInjector
    fun contributeCreateAccountActivity() : CreateAccountActivity
}