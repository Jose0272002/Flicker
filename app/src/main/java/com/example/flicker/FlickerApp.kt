package com.example.flicker

import android.app.Application
import appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FlickerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Aquí es donde Koin se inicia y se prepara para toda la aplicación
        startKoin {
            // (Opcional) Un logger para ver qué está haciendo Koin en el Logcat. ¡Muy útil!
            androidLogger()
            // Provee el contexto de la aplicación a Koin
            androidContext(this@FlickerApp)
            // Carga todas las "recetas" que definiste en tus módulos
            modules(appModule)
        }
    }
}
