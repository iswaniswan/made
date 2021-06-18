package com.iswan.main.favourite

import android.content.Context
import com.iswan.main.thatchapterfan.di.FavouriteDependencies
import dagger.BindsInstance
import dagger.Component


@Component(dependencies = [FavouriteDependencies::class] )
interface FavouriteComponent {

    fun inject(fragment: FavouriteFragment)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun favouriteDependencies(favouriteDependencies: FavouriteDependencies): Builder
        fun build(): FavouriteComponent
    }

}