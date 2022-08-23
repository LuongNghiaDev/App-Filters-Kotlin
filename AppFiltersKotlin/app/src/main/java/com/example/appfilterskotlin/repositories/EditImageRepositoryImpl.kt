package com.example.appfilterskotlin.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.appfilterskotlin.data.ImageFilters
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class EditImageRepositoryImpl(private val context: Context): EditImageRepository {

    override suspend fun prepareImagePreview(imageUri: Uri): Bitmap? {
        getInputStreamFromUri(imageUri)?.let { inputStream ->
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val width = context.resources.displayMetrics.widthPixels
            val height = ((originalBitmap.height * width) / originalBitmap.width)
            return Bitmap.createScaledBitmap(originalBitmap, width, height, false)
        } ?: return null
    }

    override suspend fun getImageFilters(image: Bitmap): List<ImageFilters> {
        val gpuImage = GPUImage(context).apply {
            setImage(image)
        }
        val imageFilters: ArrayList<ImageFilters> = ArrayList()

        //region:: Image Filters

        //Normal
        GPUImageFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilters(
                    "Normal",
                    filter,
                    gpuImage.bitmapWithFilterApplied
                )
            )
        }

        //Retro
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.0f, 0.0f,0.0f,
                0.0f, 1.0f,0.2f, 0.0f,
                0.1f, 0.1f,1.0f, 0.0f,
                1.0f, 0.0f,0.0f, 1.0f,
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilters(
                    "Retro",
                    filter,
                    gpuImage.bitmapWithFilterApplied
                )
            )
        }

        //Just
        GPUImageColorMatrixFilter(
            0.9f,
            floatArrayOf(
                0.4f, 0.6f, 0.5f,0.0f,
                0.0f, 0.4f,1.0f, 0.0f,
                0.05f, 0.1f,0.4f, 0.4f,
                1.0f, 1.0f,1.0f, 1.0f,
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilters(
                    "Just",
                    filter,
                    gpuImage.bitmapWithFilterApplied
                )
            )
        }

        //Hume
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.25f, 0.0f, 0.2f,0.0f,
                0.0f, 1.0f,0.2f, 0.0f,
                0.0f, 0.3f,1.0f, 0.3f,
                0.0f, 0.0f,0.0f, 1.0f,
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilters(
                    "Hume",
                    filter,
                    gpuImage.bitmapWithFilterApplied
                )
            )
        }

        //Desert
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.6f, 0.4f, 0.2f,0.5f,
                0.0f, 0.8f,0.3f, 0.05f,
                0.3f, 0.3f,0.5f, 0.08f,
                0.0f, 0.0f,0.0f, 1.0f,
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilters(
                    "Desert",
                    filter,
                    gpuImage.bitmapWithFilterApplied
                )
            )
        }

        //Old Times
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.05f, 0.0f,0.0f,
                -0.2f, 1.1f,-0.2f, 0.11f,
                0.2f, 0.0f,1.0f, 0.0f,
                0.0f, 0.0f,0.0f, 1.0f,
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilters(
                    "Old Times",
                    filter,
                    gpuImage.bitmapWithFilterApplied
                )
            )
        }

        //Limo
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.0f, 0.08f,0.0f,
                0.4f, 1.0f,0.0f, 0.0f,
                0.0f, 0.0f,1.0f, 0.1f,
                0.0f, 0.0f,0.0f, 1.0f,
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilters(
                    "Limo",
                    filter,
                    gpuImage.bitmapWithFilterApplied
                )
            )
        }

        //Sepia
        GPUImageSepiaToneFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilters(
                    "Sepia",
                    filter,
                    gpuImage.bitmapWithFilterApplied
                )
            )
        }

        //Solar
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.5f, 0f, 0f,0f,
                0f, 1f,0f, 0f,
                0f, 0f,1f, 0f,
                0f, 0f,0f, 1f,
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilters(
                    "Solar",
                    filter,
                    gpuImage.bitmapWithFilterApplied
                )
            )
        }

        //Wole
        GPUImageSaturationFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilters(
                    "Wole",
                    filter,
                    gpuImage.bitmapWithFilterApplied
                )
            )
        }

        //Neutron
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0f, 1f, 0f,0f,
                0f, 1f,0f, 0f,
                0f, 0.6f,1f, 0f,
                0f, 0f,0f, 1f,
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilters(
                    "Neutron",
                    filter,
                    gpuImage.bitmapWithFilterApplied
                )
            )
        }

        //Bright
        GPUImageRGBFilter(1.1f, 1.3f, 1.6f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilters(
                    "Bright",
                    filter,
                    gpuImage.bitmapWithFilterApplied
                )
            )
        }

        //Milk
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.0f, 1.0f, 0.0f,0.0f,
                0.0f, 1.0f,0.0f, 0.0f,
                0.0f, 0.64f,0.5f, 0.0f,
                0.0f, 0.0f,0.0f, 1.0f,
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilters(
                    "Milk",
                    filter,
                    gpuImage.bitmapWithFilterApplied
                )
            )
        }

        //endregion

        return imageFilters
    }

    override suspend fun savedFilterImage(filteredBitmap: Bitmap): Uri? {
        return try {
            val mediaStorageDirectory = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Saved Images"
            )
            if(!mediaStorageDirectory.exists()) {
                mediaStorageDirectory.mkdirs()
            }
            val fileName = "IMG_${System.currentTimeMillis()}.jpg"
            val file = File(mediaStorageDirectory, fileName)
            saveFile(file, filteredBitmap)
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        }catch (e:Exception) {
            null
        }
    }

    private fun saveFile(file: File, bitmap: Bitmap) {
        with(FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
            flush()
            close()
        }
    }

    private fun getInputStreamFromUri(uri: Uri): InputStream? {
        return context.contentResolver.openInputStream(uri)
    }
}