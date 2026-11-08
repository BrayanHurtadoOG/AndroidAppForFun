package com.example.example.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.example.R
import com.example.example.data.MediaRepository
import com.example.example.data.PlaylistRepository
import com.example.example.data.db.UchihaDatabase
import com.example.example.ui.navigation.UchihaNavHost

@Composable
fun ExampleApp() {
    // Obtenemos el contexto actual de Android
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    // ViewModel con dependencias inyectadas por Hilt (evita instanciar en vista previa)
    val viewModel: MusicViewModel? = if (isPreview) null else hiltViewModel()

    // Estado que rastrea si el usuario concedió el permiso de audio
    var permissionGranted by remember {
        mutableStateOf(
            isPreview || (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED)
        )
    }

    // Configuramos el lanzador para pedir permisos al sistema
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
        if (isGranted) {
            // Si se concede, cargamos las canciones inmediatamente
            viewModel?.loadSongs()
        }
    }

    // Efecto que se ejecuta al iniciar la aplicación
    LaunchedEffect(Unit) {
        if (!isPreview && viewModel != null) {
            // Inicializa el controlador de reproducción (ExoPlayer)
            viewModel.initController()
            // Si ya tenemos el permiso, cargamos la lista de canciones
            if (permissionGranted) {
                viewModel.loadSongs()
            }
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
            // Si hay permisos, mostramos el grafo de navegación: la navegación
            // por estado (showNowPlaying, pestaña, playlist abierta) ahora la
            // resuelve Navigation Compose con su back stack.
            UchihaNavHost(viewModel = viewModel)
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
    }
}
