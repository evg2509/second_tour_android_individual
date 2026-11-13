package ru.nto.guestmanagement.mobile.task.a3

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nto.guestmanagement.mobile.data.PassRepository
import ru.nto.guestmanagement.mobile.model.PassScreenState

class PassLoadController(
    private val repository: PassRepository,
    private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow<PassScreenState>(PassScreenState.InitialLoading)
    val state: StateFlow<PassScreenState> = _state.asStateFlow()

    private var isRequesting = false

    fun start() {
        if (isRequesting) return
        if (_state.value is PassScreenState.InitialLoading) {
            loadData(isRefresh = false)
        }
    }

    fun retry() {
        if (isRequesting) return
        if (_state.value is PassScreenState.InitialError) {
            _state.value = PassScreenState.InitialLoading
            loadData(isRefresh = false)
        }
    }

    fun refresh() {
        if (isRequesting) return
        val currentState = _state.value
        if (currentState is PassScreenState.Content) {
            _state.value = currentState.copy(isRefreshing = true)
            loadData(isRefresh = true)
        }
    }

    private fun loadData(isRefresh: Boolean) {
        isRequesting = true
        scope.launch {
            val result = repository.load()
            isRequesting = false

            result.fold(
                onSuccess = { data ->
                    _state.value = PassScreenState.Content(data, isRefreshing = false, isOffline = false)
                },
                onFailure = { error ->
                    val errorMessage = error.message ?: "Не удалось загрузить пропуск"
                    if (isRefresh) {
                        val oldData = (_state.value as? PassScreenState.Content)?.data
                        if (oldData != null) {
                            _state.value = PassScreenState.Content(oldData, isRefreshing = false, isOffline = true)
                        }
                    } else {
                        _state.value = PassScreenState.InitialError(errorMessage)
                    }
                }
            )
        }
    }
}