package com.example.appfilterskotlin.dependencyinjection

import com.example.appfilterskotlin.viewmodels.EditImageViewModel
import com.example.appfilterskotlin.viewmodels.SavedImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
    viewModel { SavedImagesViewModel(savedImageRepository = get()) }
}