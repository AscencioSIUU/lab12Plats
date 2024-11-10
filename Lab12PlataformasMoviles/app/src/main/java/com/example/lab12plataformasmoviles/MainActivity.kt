package com.example.lab12plataformasmoviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab12plataformasmoviles.ui.theme.Lab12PlataformasMovilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Lista para almacenar las ubicaciones y fotos
            var locationList by remember { mutableStateOf(listOf<LocationPhoto>()) }
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "location_screen"
            ) {
                composable("location_screen") {
                    LocationScreen(
                        onSaveLocation = { newLocation ->
                            // Agregar nueva ubicación a la lista
                            locationList = locationList + newLocation
                        },
                        onNavigateToUbicaciones = {
                            navController.navigate("ubicaciones_screen")
                        }
                    )
                }
                composable("ubicaciones_screen") {
                    UbicacionesScreen(locations = locationList)
                }
            }
        }
    }
}


