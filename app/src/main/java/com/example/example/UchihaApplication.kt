package com.example.example

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Punto de entrada de Hilt: genera el grafo de dependencias de la app
 * (ver `di/AppModule.kt`). Declarada en el AndroidManifest.
 */
@HiltAndroidApp
class UchihaApplication : Application()
