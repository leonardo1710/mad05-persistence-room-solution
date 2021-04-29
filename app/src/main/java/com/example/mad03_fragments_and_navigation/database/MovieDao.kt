package com.example.mad03_fragments_and_navigation.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mad03_fragments_and_navigation.models.Movie

@Dao
interface MovieDao {

    @Insert
    fun create(movie: Movie) : Long
    @Update
    fun update(movie: Movie)
    @Query("DELETE FROM movie_favorites_table WHERE id = :movieId")
    fun delete(movieId: Long)

    @Query("DELETE FROM movie_favorites_table")
    fun clearTable()

    @Query("SELECT * from movie_favorites_table")
    fun getAll() : LiveData<List<Movie>>

    @Query("SELECT * from movie_favorites_table WHERE id = :movieId")
    fun get(movieId: Long): LiveData<Movie>

}