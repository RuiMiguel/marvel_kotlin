package com.everis.android.marvelkotlin.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import com.everis.android.marvelkotlin.R
import com.everis.android.marvelkotlin.presentation.home.HomeFragmentDirections
import com.everis.android.marvelkotlin.presentation.splash.SplashFragmentDirections

class AppNavigator(
    navigatorLifecycle: NavigatorLifecycle,
) :
    BaseNavigator.AppBaseNavigator(navigatorLifecycle) {

    fun goToHome() {
        val clearOptions =
            clearBackStackTo(clear = true, to = R.id.screen_splash, popUpToInclusive = true)
        val navOptions = navOptions {
            popUpTo(
                id = clearOptions?.popUpTo ?: -1,
                popUpToBuilder = {
                    inclusive = clearOptions?.isPopUpToInclusive ?: false
                }
            )
            anim {
                enter = R.anim.alpha_in
                exit = R.anim.alpha_out
            }
        }
        goTo<Fragment>(SplashFragmentDirections.actionToScreenMain(), navOptions)
    }

    fun goToDetail() {
        val navOptions = navOptions {
            anim {
                enter = R.anim.alpha_in
                exit = R.anim.alpha_out
            }
        }
        goTo<Fragment>(HomeFragmentDirections.actionToScreenDetail(), navOptions)
    }
}
