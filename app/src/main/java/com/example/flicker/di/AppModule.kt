package com.example.flicker.di

import android.content.Context
import com.example.flicker.data.repository.ChannelFirestoreRepository
import com.example.flicker.data.repository.MovieFirestoreRepository
import com.example.flicker.data.repository.UserFirestoreRepository
import com.example.flicker.data.repository.WatchlistFirestoreRepository
import com.example.flicker.domain.model.SessionManager
import com.example.flicker.domain.repository.ChannelRepository
import com.example.flicker.domain.repository.MovieRepository
import com.example.flicker.domain.repository.UserRepository
import com.example.flicker.domain.repository.WatchlistRepository
import com.example.flicker.domain.usecase.channels.GetChannelsUseCase
import com.example.flicker.domain.usecase.movies.GetMoviesByIdsUseCase
import com.example.flicker.domain.usecase.movies.GetMoviesUseCase
import com.example.flicker.domain.usecase.users.LoginUseCase
import com.example.flicker.domain.usecase.users.RegisterUseCase
import com.example.flicker.domain.usecase.watchlist.GetUserWatchlistUseCase
import com.example.flicker.domain.usecase.watchlist.ToggleWatchlistUseCase
import com.example.flicker.presentation.viewmodel.channels.ChannelsViewModel
import com.example.flicker.presentation.viewmodel.content.movies.MoviesViewModel
import com.example.flicker.presentation.viewmodel.profile.ProfileViewModel
import com.example.flicker.presentation.viewmodel.search.SearchViewModel
import com.example.flicker.presentation.viewmodel.user.LoginViewModel
import com.example.flicker.presentation.viewmodel.watchlist.WatchlistViewModel
import com.example.proyecto.presentation.viewmodel.users.RegisterViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // --- DEPENDENCIAS DE FIREBASE ---
    single { FirebaseFirestore.getInstance() }

    // --- SESIÓN Y PERSISTENCIA LOCAL ---
    // Provee una instancia única (singleton) de SharedPreferences
    single {
        androidContext().getSharedPreferences("FlickerAppPreferences", Context.MODE_PRIVATE)
    }

    // Provee una instancia única de Gson para convertir objetos
    single { Gson() }

    // Provee SessionManager, inyectando SharedPreferences y Gson que Koin ya sabe cómo crear
    single { SessionManager(get(), get()) }

    // --- REPOSITORIOS ---
    single<MovieRepository> { MovieFirestoreRepository(get()) }
    single<UserRepository> { UserFirestoreRepository(get()) }
    single<WatchlistRepository> { WatchlistFirestoreRepository(get()) }
    single<ChannelRepository> { ChannelFirestoreRepository(get()) }

    // --- CASOS DE USO (Use Cases) ---
    // Single para objetos que no tienen estado y pueden ser compartidos
    single { GetMoviesUseCase(get()) }
    single { GetChannelsUseCase(get()) }
    single { LoginUseCase(get()) }
    single { RegisterUseCase(get()) }
    // Factory para objetos que deben crearse nuevos cada vez que se solicitan
    factory { GetUserWatchlistUseCase(watchlistRepository = get(), sessionManager = get()) }
    factory { ToggleWatchlistUseCase(watchlistRepository = get(), sessionManager = get()) }
    factory { GetMoviesByIdsUseCase(movieRepository = get()) }


    // --- VIEWMODELS ---
    viewModel { MoviesViewModel(get()) }
    viewModel { ChannelsViewModel(get()) }
    viewModel { LoginViewModel(get(),get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { ProfileViewModel(sessionManager = get(), userRepository = get()) }
    viewModel {
        WatchlistViewModel(
            sessionManager = get(),
            toggleWatchlistUseCase = get(),
            getUserWatchlistUseCase = get(),
            getMoviesByIdsUseCase = get()
        )
    }
}
