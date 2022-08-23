package com.example.appfilterskotlin.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appfilterskotlin.databinding.ItemContainerSavedBinding
import com.example.appfilterskotlin.listeners.SavedImageListener
import java.io.File

class SavedImageAdpater(private val savedImages: List<Pair<File, Bitmap>>,
private val savedImagesListener: SavedImageListener):
RecyclerView.Adapter<SavedImageAdpater.MyHolder>(){

    inner class MyHolder(val binding: ItemContainerSavedBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = ItemContainerSavedBinding.inflate(LayoutInflater.from(parent.context),
        parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        with(holder) {
            with(savedImages[position]) {
                binding.imageSaved.setImageBitmap(second)
                binding.imageSaved.setOnClickListener {
                    savedImagesListener.onImagedClicked(first)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return savedImages.size
    }


}