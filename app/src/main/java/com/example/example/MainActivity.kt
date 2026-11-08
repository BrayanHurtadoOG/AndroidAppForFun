package com.example.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.example.ui.ExampleApp
import com.example.example.ui.theme.ExampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita el modo de pantalla completa (borde a borde)
        enableEdgeToEdge()
        setContent {
            // Aplica el tema de la aplicación
            ExampleTheme {
                // Llama al componente raíz de la interfaz
                ExampleApp()
            }
        }
    }
}

@Preview
@Composable
fun ExampleAppPreview() {
    ExampleTheme {
        ExampleApp()
    }
}