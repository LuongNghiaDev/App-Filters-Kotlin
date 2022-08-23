package com.example.appfilterskotlin.activities

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.appfilterskotlin.adapters.ImageFiltersAdapter
import com.example.appfilterskotlin.data.ImageFilters
import com.example.appfilterskotlin.databinding.ActivityEditImageBinding
import com.example.appfilterskotlin.listeners.ImageFilterListener
import com.example.appfilterskotlin.utilities.displayToast
import com.example.appfilterskotlin.utilities.show
import com.example.appfilterskotlin.viewmodels.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity() , ImageFilterListener{

    companion object {
        const val KEY_FILTERED_IMAGE_URI = "filteredImageUri"
    }

    private lateinit var binding: ActivityEditImageBinding

    private val viewModel: EditImageViewModel by viewModel()
    private lateinit var gpuImage: GPUImage

    //Image Bitmap
    private lateinit var originalBitmap: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setUpObservers()
        prepareImagePreview()
    }

    private fun setUpObservers() {
        viewModel.imagePreviewUiState.observe(this) {
            val dataState = it ?: return@observe
            binding.previewProgressBar.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->
                originalBitmap = bitmap
                filteredBitmap.value = bitmap

                with(originalBitmap) {
                    gpuImage.setImage(this)
                    binding.imagePreview.show()
                    viewModel.loadImageFilters(this)
                }

            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
        viewModel.imageFiltersUiState.observe(this) {
            val imageFilterDataState = it ?: return@observe
            binding.ImageFiltersProgressBar.visibility =
                if(imageFilterDataState.isLoading) View.VISIBLE else View.GONE
            imageFilterDataState.imageFilters?.let { imageFilters ->
                ImageFiltersAdapter(imageFilters, this).also { adapter ->
                    binding.filtersRecyclerView.adapter = adapter
                }
            } ?: kotlin.run {
                imageFilterDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
        filteredBitmap.observe(this) { bitmap ->
            binding.imagePreview.setImageBitmap(bitmap)
        }

        viewModel.saveFilterImageUiState.observe(this) {
            val saveFilter = it ?: return@observe
            if(saveFilter.isLoading) {
                binding.saveProgressBar.visibility = View.VISIBLE
                binding.imageDone.visibility = View.GONE
            } else {
                binding.saveProgressBar.visibility = View.GONE
                binding.imageDone.visibility = View.VISIBLE
            }
            saveFilter.uri?.let { saveImageUri ->
                Intent(
                    applicationContext,
                    FilteredImageActivity::class.java
                ).also { filterdImageIntent ->
                    filterdImageIntent.putExtra(KEY_FILTERED_IMAGE_URI, saveImageUri)
                    startActivity(filterdImageIntent)
                }
            } ?: kotlin.run {
                saveFilter.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }

    private fun prepareImagePreview() {
        gpuImage = GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URL)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
        binding.imageDone.setOnClickListener {
            filteredBitmap.value?.let { bitmap ->
                viewModel.saveFilterImage(bitmap)
            }
        }

        binding.imagePreview.setOnLongClickListener {
            binding.imagePreview.setImageBitmap(originalBitmap)
            return@setOnLongClickListener true
        }
        binding.imagePreview.setOnClickListener {
            binding.imagePreview.setImageBitmap(filteredBitmap.value)
        }
    }

    override fun onFilterSelected(imageFilter: ImageFilters) {
        with(imageFilter) {
            with(gpuImage) {
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }
}