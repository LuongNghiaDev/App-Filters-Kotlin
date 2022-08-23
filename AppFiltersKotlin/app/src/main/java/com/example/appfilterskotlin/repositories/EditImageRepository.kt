package com.example.appfilterskotlin.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.example.appfilterskotlin.data.ImageFilters

interface EditImageRepository {
    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?
    suspend fun getImageFilters(image: Bitmap): List<ImageFilters>
    suspend fun savedFilterImage(filteredBitmap: Bitmap): Uri?
}