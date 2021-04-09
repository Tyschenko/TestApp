package me.tyschenko.revoluttestapp.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.tyschenko.revoluttestapp.presenter.CurrencyActivityPresenter
import me.tyschenko.revoluttestapp.presenter.CurrencyActivityPresenterImpl
import me.tyschenko.revoluttestapp.view.CurrencyActivity
import me.tyschenko.revoluttestapp.view.CurrencyActivityView

@Module
interface ActivityBuilder {
    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivityPresenterModule::class])
    fun provideCurrencyActivity(): CurrencyActivity
}

@Module
private interface ActivityPresenterModule {
    @ActivityScope
    @Binds
    fun bindView(view: CurrencyActivity): CurrencyActivityView

    @ActivityScope
    @Binds
    fun bindPresenter(presenter: CurrencyActivityPresenterImpl): CurrencyActivityPresenter
}