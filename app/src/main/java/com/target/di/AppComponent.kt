package com.target.di

import com.target.TargetApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    NetworkModule::class,
    BuildersModule::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(targetApplication: TargetApplication): AppComponent.Builder
        fun build(): AppComponent
    }

    fun inject(targetApplication: TargetApplication)
}
