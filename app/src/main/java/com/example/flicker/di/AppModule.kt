

import com.example.flicker.data.repository.MovieFirestoreRepository
import com.example.flicker.data.repository.UserFirestoreRepository
import com.example.flicker.domain.repository.MovieRepository
import com.example.flicker.domain.repository.UserRepository
import com.example.flicker.domain.usecase.movies.GetMoviesUseCase
import com.example.flicker.domain.usecase.users.LoginUseCase
import com.example.flicker.presentation.viewmodel.login.LoginViewModel
import com.example.flicker.presentation.viewmodel.movies.MoviesViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // --- DEPENDENCIAS DE FIREBASE ---
    single { FirebaseFirestore.getInstance() }

    // --- REPOSITORIOS ---
    single<MovieRepository> { MovieFirestoreRepository(get()) }
    single<UserRepository> { UserFirestoreRepository(get()) }

    // --- CASOS DE USO ---
    single { GetMoviesUseCase(get()) }
    single { LoginUseCase(get()) }

    // --- VIEWMODELS ---
    viewModel { MoviesViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}