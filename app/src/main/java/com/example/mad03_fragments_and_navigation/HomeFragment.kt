package com.example.mad03_fragments_and_navigation

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.mad03_fragments_and_navigation.adapters.MovieListAdapter
import com.example.mad03_fragments_and_navigation.database.AppDatabase
import com.example.mad03_fragments_and_navigation.databinding.FragmentHomeBinding
import com.example.mad03_fragments_and_navigation.models.Movie
import com.example.mad03_fragments_and_navigation.models.MovieStore
import com.example.mad03_fragments_and_navigation.repositories.MovieRepository
import com.example.mad03_fragments_and_navigation.viewmodels.MovieFavoritesViewModel
import com.example.mad03_fragments_and_navigation.viewmodels.MovieFavoritesViewModelFactory


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MovieFavoritesViewModel by viewModels {
        val repository = MovieRepository.getInstance(AppDatabase.getDatabase(requireContext()).movieDao())  // normally these stuff should be injected from a central provider class or object
        MovieFavoritesViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        setHasOptionsMenu(true) // enable the options menu in the action bar

        val adapter = MovieListAdapter(
            onAddToFavsClicked = { movie: Movie -> addToFavorites(movie)}
        )    // instantiate a new MovieListAdapter for recyclerView
        binding.movieList.adapter = adapter // assign adapter to the recyclerView

        subscribeUI(adapter)

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

    private fun addToFavorites(movie: Movie){
        viewModel.addToFavorites(movie)
    }

    private fun subscribeUI(adapter: MovieListAdapter){
        val movieList = MovieStore()
        adapter.submitList(movieList.defaultMovies) // submit the statically generated movielist to the recyclerView
    }

    /**
     * Navigation behavior if options menu item is clicked
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.
        onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    /**
     * inflate the options menu
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }
}