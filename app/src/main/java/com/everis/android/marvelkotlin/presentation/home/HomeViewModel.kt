package com.everis.android.marvelkotlin.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.everis.android.marvelkotlin.domain.model.Character
import com.everis.android.marvelkotlin.domain.usecase.GetCharacters
import com.everis.android.marvelkotlin.presentation.navigation.AppNavigator
import com.everis.android.marvelkotlin.presentation.viewmodel.BaseViewModel
import com.everis.android.marvelkotlin.presentation.viewmodel.Result
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.internal.notifyAll

class HomeViewModel constructor(
    dispatcher: CoroutineDispatcher,
    private val appNavigator: AppNavigator,
    private val getCharacters: GetCharacters
) : BaseViewModel(dispatcher) {

    private var currentLimit = 50
    private var currentOffset = 0

    private var _charactersBackup: MutableList<Character> = mutableListOf()

    private var _characters = MutableLiveData<Result<List<Character>?>>()
    val characters: LiveData<Result<List<Character>?>>
        get() = _characters

    init {
        requestAllCharacters()
    }

    private fun requestAllCharacters() {
        launch {
            _characters.postValue(Result.Loading())

            currentOffset = 0

            getCharacters().fold(
                {
                    _characters.postValue(Result.Error(it))
                },
                {
                    _charactersBackup.addAll(it)
                    _characters.postValue(Result.Success(it))
                }
            )
        }
    }

    fun requestMoreCharacters() {
        launch {
            _characters.postValue(Result.Loading())

            currentOffset += currentLimit;

            val params =
                GetCharacters.Params.forFilter(limit = currentLimit, offset = currentOffset)
            getCharacters(params).fold(
                {
                    _characters.postValue(Result.Error(it))
                },
                {
                    _charactersBackup.addAll(it)
                    _characters.postValue(Result.Success(_charactersBackup))
                }
            )
        }
    }

    fun selectCharacter(character: Character) {
        appNavigator.goToDetail(character.id)
    }
}