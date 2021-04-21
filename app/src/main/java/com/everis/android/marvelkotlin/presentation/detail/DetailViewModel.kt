package com.everis.android.marvelkotlin.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.everis.android.marvelkotlin.domain.model.Character
import com.everis.android.marvelkotlin.domain.usecase.GetCharacter
import com.everis.android.marvelkotlin.presentation.navigation.AppNavigator
import com.everis.android.marvelkotlin.presentation.viewmodel.BaseViewModel
import com.everis.android.marvelkotlin.presentation.viewmodel.Result
import kotlinx.coroutines.CoroutineDispatcher

class DetailViewModel constructor(
    dispatcher: CoroutineDispatcher,
    private val appNavigator: AppNavigator,
    private val getCharacter: GetCharacter
) : BaseViewModel(dispatcher) {

    private var _character = MutableLiveData<Result<Character?>>()
    val character: LiveData<Result<Character?>>
        get() = _character

    fun requestForCharacter(characterId: Int) {
        launch {
            _character.postValue(Result.Loading())

            getCharacter(GetCharacter.Params.forId(characterId)).fold(
                {
                    _character.postValue(Result.Error(it))
                },
                {
                    _character.postValue(Result.Success(it))
                }
            )
        }
    }
}