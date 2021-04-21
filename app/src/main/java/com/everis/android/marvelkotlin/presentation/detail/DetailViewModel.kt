package com.everis.android.marvelkotlin.presentation.detail

import com.everis.android.marvelkotlin.domain.usecase.GetCharacter
import com.everis.android.marvelkotlin.presentation.navigation.AppNavigator
import com.everis.android.marvelkotlin.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher

class DetailViewModel constructor(
    dispatcher: CoroutineDispatcher,
    private val appNavigator: AppNavigator,
    private val getCharacter: GetCharacter
) : BaseViewModel(dispatcher) {


}