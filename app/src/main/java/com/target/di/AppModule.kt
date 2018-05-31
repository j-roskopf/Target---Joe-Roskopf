package com.target.di

import android.app.Application
import android.content.Context
import com.target.TargetApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideApplication(application: TargetApplication): Application {
        return application
    }

    @Provides
    internal fun provideContext(application: TargetApplication): Context {
        return application.applicationContext
    }

}