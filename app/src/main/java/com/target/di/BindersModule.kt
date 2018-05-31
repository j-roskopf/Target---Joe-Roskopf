package com.target.di

import com.target.dealbrowserpoc.dealbrowser.deals.DealListFragment
import com.target.dealbrowserpoc.dealbrowser.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun bindsMainFragment(): DealListFragment
}