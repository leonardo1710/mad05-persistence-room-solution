package com.example.mad03_fragments_and_navigation.repositories

import com.example.mad03_fragments_and_navigation.database.MovieDao
import com.example.mad03_fragments_and_navigation.models.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(private val movieDao: MovieDao) {
    fun getMovie(id: Long) = movieDao.get(id)

    fun getAllFavorites() = movieDao.getAll()

    suspend fun addFavorite(movie: Movie) =
        withContext(Dispatchers.IO){
            val id = movieDao.create(movie)
            id
        }


    suspend fun deleteFavorite(id: Long) =
        withContext(Dispatchers.IO){
            movieDao.delete(id)
        }

    suspend fun clearTable() = withContext(Dispatchers.IO){
        movieDao.clearTable()
    }

    suspend fun updateFavorite(movie: Movie) =
        withContext(Dispatchers.IO){
            movieDao.update(movie)
        }

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: MovieRepository? = null

        fun getInstance(dao: MovieDao) =
            instance ?: synchronized(this) {
                instance ?: MovieRepository(dao).also { instance = it }
            }
    }
}