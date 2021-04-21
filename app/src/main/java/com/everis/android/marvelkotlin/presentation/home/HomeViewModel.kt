package com.everis.android.marvelkotlin.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.everis.android.marvelkotlin.domain.model.Character
import com.everis.android.marvelkotlin.domain.usecase.GetCharacters
import com.everis.android.marvelkotlin.presentation.navigation.AppNavigator
import com.everis.android.marvelkotlin.presentation.viewmodel.BaseViewModel
import com.everis.android.marvelkotlin.presentation.viewmodel.Result
import kotlinx.coroutines.CoroutineDispatcher

class HomeViewModel constructor(
    dispatcher: CoroutineDispatcher,
    private val appNavigator: AppNavigator,
    private val getCharacters: GetCharacters
) : BaseViewModel(dispatcher) {

    private var currentLimit = 50
    private var currentOffset = 0

    private var _characters = MutableLiveData<Result<List<Character>?>>()
    val characters: LiveData<Result<List<Character>?>>
        get() = _characters

    init {
        requestAllCharacters()
    }

    fun requestAllCharacters() {
        launch {
            _characters.postValue(Result.Loading())

            currentOffset = 0

            getCharacters().fold(
                {
                    _characters.postValue(Result.Error(it))
                },
                {
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
                    var localCharacters = when (val list = _characters.value) {
                        is Result.Success -> {
                            list.data?.toMutableList()?.apply { addAll(it) } ?: it
                        }
                        else -> it
                    }
                    _characters.postValue(Result.Success(localCharacters))
                }
            )
        }
    }
}