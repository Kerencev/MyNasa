package com.kerencev.mynasa.model.di

import com.kerencev.mynasa.model.repository.Repository
import com.kerencev.mynasa.model.repository.RepositoryImpl
import com.kerencev.mynasa.ui.earth.EarthViewModel
import com.kerencev.mynasa.ui.mars.MarsViewModel
import com.kerencev.mynasa.ui.photooftheday.PhotoOfTheDayViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    //Repository
    single<Repository> { RepositoryImpl() }

    //View Models
    viewModel { PhotoOfTheDayViewModel(get()) }
    viewModel { EarthViewModel(get()) }
    viewModel { MarsViewModel(get()) }
}