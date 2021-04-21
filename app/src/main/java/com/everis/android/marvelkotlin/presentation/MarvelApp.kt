package com.everis.android.marvelkotlin.presentation

import android.app.Application
import com.everis.android.marvelkotlin.BuildConfig
import com.everis.android.marvelkotlin.di.Property
import com.everis.android.marvelkotlin.di.baseModules
import com.everis.android.marvelkotlin.presentation.navigation.NavigatorLifecycle
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MarvelApp : Application() {
    private val navigator: NavigatorLifecycle by inject()

    override fun onCreate() {
        super.onCreate()
        initDI()
        initActivityLifecycle()
    }

    private fun initDI() {
        startKoin {
            androidContext(applicationContext)
            properties(getExtraProperties())
            printLogger()

            koin.loadModules(baseModules)
            koin.createRootScope()
        }
    }

    private fun getExtraProperties(): Map<String, String> {
        val extraProperties = HashMap<String, String>()
        extraProperties[Property.API_URL] = BuildConfig.API_URL
        extraProperties[Property.PRIVATE_KEY] = BuildConfig.PRIVATE_KEY
        extraProperties[Property.PUBLIC_KEY] = BuildConfig.PUBLIC_KEY
        extraProperties[Property.PRINT_LOGS] = BuildConfig.DEBUG.toString()
        return extraProperties
    }

    private fun initActivityLifecycle() {
        registerActivityLifecycleCallbacks(navigator.activityLifecycleCallbacks)
    }
}