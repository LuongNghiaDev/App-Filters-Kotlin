package com.example.appfilterskotlin.data

import android.graphics.Bitmap
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

data class ImageFilters(
    val name: String,
    val filter: GPUImageFilter,
    val filtersPreview: Bitmap
)