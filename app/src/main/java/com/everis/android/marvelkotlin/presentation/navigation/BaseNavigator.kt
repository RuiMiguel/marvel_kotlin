package com.everis.android.marvelkotlin.presentation.navigation

import android.net.Uri
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions

sealed class BaseNavigator(private val navigatorLifecycle: NavigatorLifecycle) {
    abstract class AppBaseNavigator(navigatorLifecycle: NavigatorLifecycle) :
        BaseNavigator(navigatorLifecycle = navigatorLifecycle)

    abstract class FeatureBaseNavigator(navigatorLifecycle: NavigatorLifecycle) :
        BaseNavigator(navigatorLifecycle = navigatorLifecycle)

    var activity: FragmentActivity?
        get() = navigatorLifecycle.activity
        set(value) {
            navigatorLifecycle.activity = value
        }

    val fragment: Fragment?
        get() = activity?.supportFragmentManager?.primaryNavigationFragment

    protected inline fun <reified Type : Fragment> canReload(reload: Boolean): Boolean {
        var canReload = !reload
        if (canReload) {
            val sameFragment = fragment?.childFragmentManager?.fragments?.find { item ->
                item is Type
            }
            canReload = sameFragment?.let { false } ?: true
        }

        return canReload
    }

    protected fun clearBackStackTo(
        clear: Boolean,
        @IdRes to: Int,
        popUpToInclusive: Boolean = false
    ): NavOptions? {
        return if (clear) {
            navOptions {
                popUpTo(
                    id = to,
                    popUpToBuilder = {
                        inclusive = popUpToInclusive
                    }
                )
            }
        } else null
    }

    inline fun <reified Type : Fragment> goBack() {
        fragment?.getChild<Type>()?.getParent()?.findNavController()
            ?.popBackStack() ?: activity?.finish()
    }

    inline fun <reified Type : Fragment> goTo(
        @IdRes id: Int,
        navOptions: NavOptions? = null,
        extras: Bundle
    ) {
        fragment?.getChild<Type>()?.findNavController()?.navigate(id, extras, navOptions)
    }

    inline fun <reified Type : Fragment> goTo(
        directions: NavDirections,
        navOptions: NavOptions? = null
    ) {
        fragment?.getChild<Type>()?.findNavController()?.navigate(directions, navOptions)
    }

    inline fun <reified Type : Fragment> goTo(
        uri: Uri,
        navOptions: NavOptions? = null
    ) {
        fragment?.getChild<Type>()?.findNavController()?.navigate(uri, navOptions)
    }

    inline fun <reified Type : Fragment> Fragment.getChild(): Fragment? {
        var fragment: Fragment? = this
        var child: Fragment? = if (fragment is Type) fragment else null
        while (child == null && fragment?.childFragmentManager?.fragments?.isNotEmpty() == true) {
            child = fragment.childFragmentManager.fragments.firstOrNull { it is Type }
            fragment = fragment.childFragmentManager.primaryNavigationFragment
        }
        return child
    }

    fun Fragment.getParent() = run {
        var parent = parentFragment
        while (parent != null && parent.childFragmentManager.backStackEntryCount == 0) {
            parent = parent.parentFragment
        }
        parent
    }
}