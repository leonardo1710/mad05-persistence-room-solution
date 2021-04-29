package com.example.mad03_fragments_and_navigation.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.mad03_fragments_and_navigation.databinding.DialogTextinputBinding
import com.example.mad03_fragments_and_navigation.models.Movie
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TextInputDialog(
    val movie: Movie,
    val onPositiveClicked: (Movie, String) -> Unit,
): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            val builder = MaterialAlertDialogBuilder(activity)

            val binding = DialogTextinputBinding.inflate(layoutInflater)
            binding.textInput.hint = "Your notes"
            builder.setView(binding.root)
                .setTitle("Add a note")
                .setPositiveButton("Save") { _, _ ->
                    onPositiveClicked(movie, binding.textInput.text.toString())
                }
            val dialog = builder.create()
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}