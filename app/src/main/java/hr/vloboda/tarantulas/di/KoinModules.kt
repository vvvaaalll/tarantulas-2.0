package hr.vloboda.tarantulas.di

import hr.vloboda.tarantulas.repository.TarantulasRepository
import hr.vloboda.tarantulas.authentication.viewModel.AuthenticationViewModel
import hr.vloboda.tarantulas.repository.ImageRepository
import hr.vloboda.tarantulas.tarantulas.TarantulasViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { TarantulasRepository() }
    single { ImageRepository() }
}

val viewModelModule = module {
    viewModel { AuthenticationViewModel() }
    viewModel { TarantulasViewModel(get()) }
}