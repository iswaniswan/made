package com.iswan.main.subscription

import android.content.Context
import com.iswan.main.thatchapterfan.di.SubscriptionDependencies
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [SubscriptionDependencies::class])
interface SubscriptionComponent {

    fun inject(activity: SubscriptionActivity)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun subscriptionDependencies(subscriptionDependencies: SubscriptionDependencies): Builder
        fun build(): SubscriptionComponent
    }

}