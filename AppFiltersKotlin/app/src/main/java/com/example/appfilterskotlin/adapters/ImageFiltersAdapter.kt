package com.example.appfilterskotlin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.appfilterskotlin.R
import com.example.appfilterskotlin.data.ImageFilters
import com.example.appfilterskotlin.databinding.ItemContainerFilterBinding
import com.example.appfilterskotlin.listeners.ImageFilterListener

class ImageFiltersAdapter(
    private val imageFilters: List<ImageFilters>,
    private val imageFilterListener: ImageFilterListener
    ): RecyclerView.Adapter<ImageFiltersAdapter.MyHolder>(){

    private var selectedFilterPosition = 0
    private var previousSelectedPositon = 0

    inner class MyHolder(val binding: ItemContainerFilterBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = ItemContainerFilterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(imageFilters[position]) {
                binding.imageFilterPreview.setImageBitmap(filtersPreview)
                binding.textFilterName.text = name
                binding.root.setOnClickListener {
                    if(position != selectedFilterPosition) {
                        imageFilterListener.onFilterSelected(this)
                        previousSelectedPositon = selectedFilterPosition
                        selectedFilterPosition = position
                        with(this@ImageFiltersAdapter) {
                            notifyItemChanged(previousSelectedPositon, Unit)
                            notifyItemChanged(selectedFilterPosition, Unit)
                        }
                    }
                }
            }
            binding.textFilterName.setTextColor(
                ContextCompat.getColor(
                    binding.textFilterName.context,
                    if(selectedFilterPosition == position)
                        R.color.primaryDark
                    else
                        R.color.primaryText
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return imageFilters.size
    }


}