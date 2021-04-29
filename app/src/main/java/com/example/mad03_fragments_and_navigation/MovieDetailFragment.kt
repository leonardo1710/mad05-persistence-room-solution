package com.example.mad03_fragments_and_navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mad03_fragments_and_navigation.database.AppDatabase
import com.example.mad03_fragments_and_navigation.databinding.FragmentMovieDetailBinding
import com.example.mad03_fragments_and_navigation.models.MovieStore
import com.example.mad03_fragments_and_navigation.repositories.MovieRepository
import com.example.mad03_fragments_and_navigation.viewmodels.MovieFavoritesViewModel
import com.example.mad03_fragments_and_navigation.viewmodels.MovieFavoritesViewModelFactory

class MovieDetailFragment : Fragment() {
    private lateinit var binding: FragmentMovieDetailBinding

    private val viewModel: MovieFavoritesViewModel by viewModels {
        val repository = MovieRepository.getInstance(AppDatabase.getDatabase(requireContext()).movieDao())  // normally these stuff should be injected from a central provider class or object
        MovieFavoritesViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container, false)

        val args = MovieDetailFragmentArgs.fromBundle(requireArguments())   // get navigation arguments

        val movieEntry = MovieStore().findMovieByUUID(args.movieId)
        when(movieEntry){
            null -> {
                Toast.makeText(requireContext(), "Could not load movie data", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            else -> binding.movie = movieEntry
        }

        binding.addToFavorites.setOnClickListener {
            movieEntry?.let {
                viewModel.addToFavorites(movieEntry)
            }
        }

        viewModel.operationState.observe(viewLifecycleOwner, { state ->
            when(state){
                MovieFavoritesViewModel.OperationState.SUCCESS -> showToastAfterOperation("Movie saved to favorites.")
                MovieFavoritesViewModel.OperationState.FAILED -> showToastAfterOperation("Problems while saving movie to favorites.")
                else -> {}
            }
        })


        return binding.root
    }

    private fun showToastAfterOperation(text: String){
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        viewModel.resetOperationState()
    }
}