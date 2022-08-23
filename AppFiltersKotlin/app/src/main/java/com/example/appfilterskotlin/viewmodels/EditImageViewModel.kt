package com.example.appfilterskotlin.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appfilterskotlin.data.ImageFilters
import com.example.appfilterskotlin.repositories.EditImageRepository
import com.example.appfilterskotlin.utilities.Coroutines

class EditImageViewModel(private val editImageRepository: EditImageRepository): ViewModel() {

    //region:: Prepare image preview
    private val imagePreviewDataState = MutableLiveData<ImagePreviewDataState>()
    val imagePreviewUiState: LiveData<ImagePreviewDataState> get() = imagePreviewDataState

    fun prepareImagePreview(imageUri: Uri) {
        Coroutines.io {
            runCatching {
                emitUiState(isLoading = true)
                editImageRepository.prepareImagePreview(imageUri)
            }.onSuccess { bitmap ->
                if(bitmap != null) {
                    emitUiState(bitmap = bitmap)
                } else {
                    emitUiState(error = "Unable to prepare image preview")
                }
            }.onFailure {
                emitUiState(error = it.message.toString())
            }
        }
    }

    private fun emitUiState(
        isLoading: Boolean = false,
        bitmap: Bitmap? = null,
        error: String? = null
    ) {
        val dataState = ImagePreviewDataState(isLoading, bitmap, error)
        imagePreviewDataState.postValue(dataState)
    }

    data class ImagePreviewDataState(
        val isLoading: Boolean,
        val bitmap: Bitmap?,
        val error: String?
    )
    //endregion

    //region:: Load image filter
    private val imageFiltersDataState = MutableLiveData<ImageFiltersDataState>()
    val imageFiltersUiState: LiveData<ImageFiltersDataState> get() = imageFiltersDataState

    fun loadImageFilters(originalImage: Bitmap) {
        Coroutines.io {
            runCatching {
                emitImageFilterUiState(isLoading = true)
                editImageRepository.getImageFilters(getPreviewImage(originalImage))
            }.onSuccess { imageFilter ->
                emitImageFilterUiState(imageFilters = imageFilter)
            }.onFailure {
                emitImageFilterUiState(error = it.message.toString())
            }
        }
    }

    private fun getPreviewImage(originalImage: Bitmap): Bitmap {
        return runCatching {
            val previewWidth = 150
            val previewHight = originalImage.height * previewWidth / originalImage.width
            Bitmap.createScaledBitmap(originalImage, previewWidth, previewHight, false)
        }.getOrDefault(originalImage)
    }

    private fun emitImageFilterUiState(
        isLoading: Boolean = false,
        imageFilters: List<ImageFilters>? = null,
        error: String? = null
    ) {
        val dataState = ImageFiltersDataState(isLoading, imageFilters, error)
        imageFiltersDataState.postValue(dataState)
    }

    data class ImageFiltersDataState(
        val isLoading: Boolean,
        val imageFilters: List<ImageFilters>?,
        val error: String?
    )

    //endregion

    //region:: Save filtered image
    private val saveFilterImageDataState = MutableLiveData<SaveFilterImageDataState>()
    val saveFilterImageUiState: LiveData<SaveFilterImageDataState> get() = saveFilterImageDataState

    fun saveFilterImage(filteredBitmap: Bitmap) {
        Coroutines.io {
            runCatching {
                emitSaveFilterImageUiState(true)
                editImageRepository.savedFilterImage(filteredBitmap)
            }.onSuccess { saveImage ->
                emitSaveFilterImageUiState(uri = saveImage)
            }.onFailure {
                emitSaveFilterImageUiState(error = it.message.toString())
            }
        }
    }

    private fun emitSaveFilterImageUiState(
        isLoading: Boolean = false,
        uri: Uri? = null,
        error: String? = null
    ) {
        val dataState = SaveFilterImageDataState(isLoading, uri, error)
        saveFilterImageDataState.postValue(dataState)
    }

    data class SaveFilterImageDataState(
        val isLoading: Boolean,
        val uri: Uri?,
        val error: String?
    )

    //endregion
}