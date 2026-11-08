package com.example.example.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.example.ui.MusicPlayerScreen
import com.example.example.ui.MusicViewModel
import com.example.example.ui.NowPlayingScreen
import com.example.example.ui.PlaylistDetailScreen

/**
 * Rutas de Uchiha Player (Navigation Compose).
 *
 * Antes, la navegación vivía en estados de Compose repartidos por las pantallas
 * (`showNowPlaying` en [com.example.example.ui.ExampleApp], la pestaña activa en
 * `MusicPlayerScreen`, la playlist abierta en `PlaylistsTab`). Ahora cada pantalla
 * es un destino y el botón atrás del sistema lo resuelve el back stack.
 */
object UchihaDestinations {
    /** Lista de canciones (pestaña Canciones). Destino inicial. */
    const val SONGS = "songs"

    /** Lista de playlists (pestaña Playlists). */
    const val PLAYLISTS = "playlists"

    /** Pantalla completa de Now Playing. */
    const val NOW_PLAYING = "now_playing"

    /** Detalle de una playlist. */
    const val PLAYLIST_DETAIL = "playlist_detail"

    /** Argumento de [PLAYLIST_DETAIL]: id (Long) de la playlist en Room. */
    const val PLAYLIST_ID_ARG = "playlistId"

    /** Ruta del detalle de una playlist concreta. */
    fun playlistDetail(playlistId: Long): String = "$PLAYLIST_DETAIL/$playlistId"
}

@Composable
fun UchihaNavHost(
    viewModel: MusicViewModel,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = UchihaDestinations.SONGS,
        modifier = modifier
    ) {
        composable(UchihaDestinations.SONGS) {
            MusicPlayerScreen(
                viewModel = viewModel,
                onOpenNowPlaying = { navController.navigate(UchihaDestinations.NOW_PLAYING) },
                initialTab = 0,
                onTabSelected = { tab ->
                    if (tab == 1) navController.navigateToTab(UchihaDestinations.PLAYLISTS)
                },
                onPlaylistClick = { playlist ->
                    navController.navigate(UchihaDestinations.playlistDetail(playlist.id))
                }
            )
        }

        composable(UchihaDestinations.PLAYLISTS) {
            MusicPlayerScreen(
                viewModel = viewModel,
                onOpenNowPlaying = { navController.navigate(UchihaDestinations.NOW_PLAYING) },
                initialTab = 1,
                onTabSelected = { tab ->
                    if (tab == 0) navController.navigateToTab(UchihaDestinations.SONGS)
                },
                onPlaylistClick = { playlist ->
                    navController.navigate(UchihaDestinations.playlistDetail(playlist.id))
                }
            )
        }

        composable(
            route = "${UchihaDestinations.PLAYLIST_DETAIL}/{${UchihaDestinations.PLAYLIST_ID_ARG}}",
            arguments = listOf(
                navArgument(UchihaDestinations.PLAYLIST_ID_ARG) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val playlistId =
                backStackEntry.arguments?.getLong(UchihaDestinations.PLAYLIST_ID_ARG) ?: -1L
            val playlists by viewModel.playlists.collectAsState()
            val playlist = playlists.find { it.id == playlistId }
            if (playlist != null) {
                PlaylistDetailScreen(
                    viewModel = viewModel,
                    playlist = playlist,
                    onBack = { navController.popBackStack() }
                )
            } else {
                // La playlist ya no existe (p. ej. se borró): no crashear, volver atrás.
                PlaylistNotFound(onBack = { navController.popBackStack() })
            }
        }

        composable(UchihaDestinations.NOW_PLAYING) {
            NowPlayingScreen(
                viewModel = viewModel,
                onClose = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Navegación entre pestañas: un solo destino de pestaña en el back stack, con
 * estado guardado/restaurado, para que atrás desde una pestaña vuelva a la
 * anterior en vez de acumular pestañas.
 */
private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun PlaylistNotFound(onBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Playlist no encontrada")
            Button(onClick = onBack) {
                Text(text = "Volver")
            }
        }
    }
}
