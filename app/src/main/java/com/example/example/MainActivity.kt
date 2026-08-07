package com.example.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.example.ui.ExampleApp
import com.example.example.ui.theme.ExampleTheme

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
