package com.example.lab12plataformasmoviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppContent()
        }
    }
}
@Composable
fun MyAppContent() {
    var locationList by remember { mutableStateOf(listOf<LocationPhoto>()) }
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color(0xFFFAF9F6)),
        content = { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "location_screen",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Aplica el padding recibido
            ) {
                composable("location_screen") {
                    LocationScreen(
                        onSaveLocation = { newLocation ->
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
    )
}


@Preview(showBackground = true)
@Composable
fun previewStuff() {
    MyAppContent() // Reutilizamos MyAppContent para el Preview
}
