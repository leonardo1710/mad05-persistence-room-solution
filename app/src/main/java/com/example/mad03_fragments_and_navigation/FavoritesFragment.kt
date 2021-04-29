package com.example.mad03_fragments_and_navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mad03_fragments_and_navigation.adapters.FavoritesListAdapter
import com.example.mad03_fragments_and_navigation.database.AppDatabase
import com.example.mad03_fragments_and_navigation.databinding.FragmentFavoritesBinding
import com.example.mad03_fragments_and_navigation.dialogs.TextInputDialog
import com.example.mad03_fragments_and_navigation.models.Movie
import com.example.mad03_fragments_and_navigation.repositories.MovieRepository
import com.example.mad03_fragments_and_navigation.viewmodels.MovieFavoritesViewModel
import com.example.mad03_fragments_and_navigation.viewmodels.MovieFavoritesViewModelFactory


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding

    private val viewModel: MovieFavoritesViewModel by viewModels {
        val repository = MovieRepository.getInstance(AppDatabase.getDatabase(requireContext()).movieDao())  // normally these stuff should be injected from a central provider class or object
        MovieFavoritesViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)

        val adapter = FavoritesListAdapter(
            dataSet = listOf(),
            onDeleteClicked = {movieId -> onDeleteMovieClicked(movieId)},
            onEditClicked = {movie -> onEditMovieClicked(movie)},
        )

        with(binding){
            recyclerView.adapter = adapter
            clearBtn.setOnClickListener {
                viewModel.clearFavorites()
            }
        }

        viewModel.favorites.observe(viewLifecycleOwner, { favorites ->
            if(favorites != null){
                Log.i("FavoritesFragment", favorites.toString())
                adapter.updateDataSet(favorites)
            }
        })

        return binding.root
    }

    private fun addNote(movie: Movie, note: String){
       viewModel.updateNote(movie, note)
    }

    private fun onEditMovieClicked(movieObj: Movie){
        val dialog = TextInputDialog(
            movie = movieObj,
            onPositiveClicked = { movie: Movie, note: String -> addNote(movie, note)}
        )
        dialog.show(childFragmentManager, "TextInputDialog")
    }

    private fun onDeleteMovieClicked(movieId: Long){
        viewModel.removeFromFavorites(movieId)
    }
}