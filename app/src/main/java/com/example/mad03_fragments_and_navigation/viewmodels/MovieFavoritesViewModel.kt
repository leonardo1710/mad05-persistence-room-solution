package com.example.mad03_fragments_and_navigation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mad03_fragments_and_navigation.models.Movie
import com.example.mad03_fragments_and_navigation.repositories.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieFavoritesViewModel(
    private val repository: MovieRepository
): ViewModel() {
    enum class OperationState {
        INITIAL,            // Initial state
        FAILED,             // A database operation failed
        SUCCESS             // A database operation succeeded
    }

    val favorites : LiveData<List<Movie>> = repository.getAllFavorites()

    private var _operationState = MutableLiveData<OperationState>()
    val operationState: LiveData<OperationState>
        get() = _operationState


    init {
        _operationState.value = OperationState.INITIAL
    }

    fun clearFavorites(){
        viewModelScope.launch {
            repository.clearTable()
        }
    }

    fun updateNote(movie: Movie, note: String){
        viewModelScope.launch {
            movie.note = note
            repository.updateFavorite(movie)
        }
    }

    fun removeFromFavorites(id: Long){
        viewModelScope.launch {
            repository.deleteFavorite(id)
        }
    }

    fun addToFavorites(movie: Movie){
        viewModelScope.launch(Dispatchers.Main) {
            movie.id = null
            val id = repository.addFavorite(movie)
            if(id > 0){
                _operationState.value = OperationState.SUCCESS
            } else {
                _operationState.value = OperationState.FAILED
            }
        }
    }

    fun resetOperationState(){
        _operationState.value = OperationState.INITIAL
    }
}