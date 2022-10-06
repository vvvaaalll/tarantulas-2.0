package hr.vloboda.tarantulas.di

import hr.vloboda.tarantulas.authentication.viewModel.AuthenticationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { AuthenticationViewModel() }

}