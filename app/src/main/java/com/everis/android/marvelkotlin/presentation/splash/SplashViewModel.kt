package com.everis.android.marvelkotlin.presentation.splash

import com.everis.android.marvelkotlin.domain.usecase.DoWait
import com.everis.android.marvelkotlin.presentation.navigation.AppNavigator
import com.everis.android.marvelkotlin.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher

class SplashViewModel constructor(
    dispatcher: CoroutineDispatcher,
    private val appNavigator: AppNavigator,
    private val doWait: DoWait,
) : BaseViewModel(dispatcher) {

    private val SPLASH_TIME = 3L

    fun load() {
        launch {
            doWait(DoWait.Params.forTime(SPLASH_TIME))
            appNavigator.goToHome()
        }
    }
}