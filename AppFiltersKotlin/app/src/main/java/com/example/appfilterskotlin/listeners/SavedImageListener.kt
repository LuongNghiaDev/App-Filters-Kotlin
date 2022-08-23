package com.example.appfilterskotlin.listeners

import java.io.File

interface SavedImageListener {
    fun onImagedClicked(file: File)
}