package com.example.appfilterskotlin.dependencyinjection

import com.example.appfilterskotlin.repositories.EditImageRepository
import com.example.appfilterskotlin.repositories.EditImageRepositoryImpl
import com.example.appfilterskotlin.repositories.SavedImageRepository
import com.example.appfilterskotlin.repositories.SavedImagesRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<EditImageRepository> { EditImageRepositoryImpl(androidContext()) }
    factory<SavedImageRepository> { SavedImagesRepositoryImpl(androidContext()) }
}