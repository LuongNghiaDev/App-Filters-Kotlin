package com.example.appfilterskotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import com.example.appfilterskotlin.R
import com.example.appfilterskotlin.adapters.SavedImageAdpater
import com.example.appfilterskotlin.databinding.ActivitySavedImageBinding
import com.example.appfilterskotlin.listeners.SavedImageListener
import com.example.appfilterskotlin.utilities.displayToast
import com.example.appfilterskotlin.viewmodels.SavedImagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class SavedImageActivity : AppCompatActivity(), SavedImageListener {

    private lateinit var binding: ActivitySavedImageBinding
    private val viewModel: SavedImagesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpObservers()
        setListeners()
        viewModel.loadSavesImage()
    }

    private fun setUpObservers() {
        viewModel.savedImageUiState.observe(this) {
            val savedImageDataState = it ?: return@observe
            binding.savedImagesProgressBar.visibility =
                if(savedImageDataState.isLoading) View.VISIBLE else View.GONE
            savedImageDataState.savedImages?.let { savedImages ->

                SavedImageAdpater(savedImages, this).also { adapter ->
                    with(binding.savedImageRecycler) {
                        this.adapter = adapter
                        visibility = View.VISIBLE
                    }
                }
            } ?: run {
                savedImageDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onImagedClicked(file: File) {
        val fileUri = FileProvider.getUriForFile(
            applicationContext,
            "${packageName}.provider",
            file
        )
        Intent(
            applicationContext,
            FilteredImageActivity::class.java
        ).also { filterImage ->
            filterImage.putExtra(EditImageActivity.KEY_FILTERED_IMAGE_URI, fileUri)
            startActivity(filterImage)
        }
    }
}