package com.example.appfilterskotlin.listeners

import com.example.appfilterskotlin.data.ImageFilters

interface ImageFilterListener {
    fun onFilterSelected(imageFilter: ImageFilters)
}