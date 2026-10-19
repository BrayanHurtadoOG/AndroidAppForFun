package com.example.example.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.example.R
import com.example.example.data.MediaRepository
import com.example.example.data.PlaylistRepository
import com.example.example.data.db.UchihaDatabase

@Composable
fun ExampleApp() {
    // Obtenemos el contexto actual de Android
    val context = LocalContext.current
    
    // Creamos y recordamos los repositorios para que no se reinicien en cada recreación
    val repository = remember { MediaRepository(context) }
    val playlistRepository = remember {
        PlaylistRepository(UchihaDatabase.get(context).playlistDao())
    }

    // Inicializamos el ViewModel con una Factory para pasarle los repositorios
    val viewModel: MusicViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MusicViewModel(repository, playlistRepository) as T
            }
        }
    )

    // Estado que rastrea si el usuario concedió el permiso de audio
    var permissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Estado que controla si se muestra la pantalla completa de "Now Playing"
    var showNowPlaying by remember { mutableStateOf(false) }

    // Configuramos el lanzador para pedir permisos al sistema
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
        if (isGranted) {
            // Si se concede, cargamos las canciones inmediatamente
            viewModel.loadSongs()
        }
    }

    // Efecto que se ejecuta al iniciar la aplicación
    LaunchedEffect(Unit) {
        // Inicializa el controlador de reproducción (ExoPlayer)
        viewModel.initController(context)
        // Si ya tenemos el permiso, cargamos la lista de canciones
        if (permissionGranted) {
            viewModel.loadSongs()
        }
    }

    // Contenedor principal Box para apilar el fondo y el contenido
    Box(modifier = Modifier.fillMaxSize()) {
        
        // 1. IMAGEN DE FONDO (Se dibuja primero para quedar abajo)
        Image(
            painter = painterResource(id = R.drawable.itachi),
            contentDescription = null, // Es decorativa, no necesita descripción
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Recorta la imagen para llenar la pantalla sin deformarse
        )

        // 2. CONTENIDO PRINCIPAL (Se dibuja encima de la imagen)
        if (permissionGranted) {
            // Si hay permisos, mostramos la pantalla del reproductor
            MusicPlayerScreen(
                viewModel = viewModel,
                onOpenNowPlaying = { showNowPlaying = true }
            )
        } else {
            // Si no hay permisos, mostramos un botón centrado para pedirlos
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {
                    // Lanza el diálogo del sistema para pedir el permiso
                    launcher.launch(Manifest.permission.READ_MEDIA_AUDIO)
                }) {
                    Text("Conceder Permiso para Música")
                }
            }
        }

        // Pantalla completa de "Now Playing", superpuesta sobre el contenido principal
        if (showNowPlaying) {
            NowPlayingScreen(
                viewModel = viewModel,
                onClose = { showNowPlaying = false }
            )
        }
    }
}
