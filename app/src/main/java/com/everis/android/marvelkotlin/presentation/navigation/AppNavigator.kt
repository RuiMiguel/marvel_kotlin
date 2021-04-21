package com.everis.android.marvelkotlin.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import com.everis.android.marvelkotlin.R
import com.everis.android.marvelkotlin.domain.model.Character
import com.everis.android.marvelkotlin.presentation.home.HomeFragmentDirections
import com.everis.android.marvelkotlin.presentation.splash.SplashFragmentDirections

class AppNavigator(
    navigatorLifecycle: NavigatorLifecycle,
) :
    BaseNavigator.AppBaseNavigator(navigatorLifecycle) {

    fun goToHome() {
        val navOptions = navOptions {

            anim {
                enter = R.anim.alpha_in
                exit = R.anim.alpha_out
            }
        }
        goTo<Fragment>(SplashFragmentDirections.actionToScreenMain(), navOptions)
        activity?.finish()
    }

    fun goToDetail(characterId: Int) {
        val navOptions = navOptions {
            anim {
                enter = R.anim.alpha_in
                exit = R.anim.alpha_out
            }
        }
        goTo<Fragment>(HomeFragmentDirections.actionToScreenDetail(characterId = characterId), navOptions)
    }
}
