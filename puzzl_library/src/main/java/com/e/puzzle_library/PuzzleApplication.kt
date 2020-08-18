package com.e.puzzl_library

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.library.di.AppComponent
import com.library.di.AppModule
import com.library.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class PuzzleApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    lateinit var component : AppComponent

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    override fun onCreate() {
        super.onCreate()
        app = this
        initDagger()
    }

    private fun initDagger(){
        component = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        component.inject(this)
    }

    companion object {
        private lateinit var app: PuzzleApplication
        fun get(): PuzzleApplication = app
    }
}