package com.kerencev.mynasa.view.main

sealed class AppState {
    data class Success<T>(val data: T) : AppState()
    object Error : AppState()
    object Loading : AppState()
}
