package com.example.lab12plataformasmoviles

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import java.io.File
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("MissingPermission")
private fun getLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationResult: (String, String) -> Unit
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            onLocationResult(location.latitude.toString(),
                location.longitude.toString())
        } else {
            onLocationResult("No location found", "No location found")
        }
    }
}


@Composable
fun LocationScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    onSaveLocation: (LocationPhoto) -> Unit,
    onNavigateToUbicaciones: () -> Unit
) {
    var latitude by remember { mutableStateOf("Unknown") }
    var longitude by remember { mutableStateOf("Unknown") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showConfirmationMessage by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val coroutineScope = rememberCoroutineScope()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getLocation(fusedLocationClient) { lat, lon ->
                latitude = lat
                longitude = lon
            }
        } else {
            latitude = "Permisos denegados"
            longitude = "Permisos denegados"
        }
    }

    val capturePhotoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            imageUri = saveImageToUri(bitmap, context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SafeTrail - Guardar Ubicación",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            color = Color.Blue,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                        getLocation(fusedLocationClient) { lat, lon ->
                            latitude = lat
                            longitude = lon
                        }
                    }
                    else -> requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Obtener Coordenadas")
        }

        Text(text = "Latitud: $latitude")
        Text(text = "Longitud: $longitude")

        Button(
            onClick = { capturePhotoLauncher.launch() },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Tomar Foto")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(200.dp)
                .border(1.dp, Color.Gray)
                .padding(16.dp)
        ) {
            Column {
                Text(text = "Vista Previa de Ubicación y Foto Guardada:")
                Text(text = "Latitud: $latitude")
                Text(text = "Longitud: $longitude")
                imageUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Imagen capturada",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(top = 8.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // Botón para guardar la ubicación y foto, y mostrar mensaje de confirmación
        Button(
            onClick = {
                val newLocation = LocationPhoto(latitude, longitude, imageUri)
                onSaveLocation(newLocation)
                showConfirmationMessage = true

                // Ocultar el mensaje después de 2 segundos
                coroutineScope.launch {
                    delay(2000)
                    showConfirmationMessage = false
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Guardar Ubicación y Foto")
        }

        // Mensaje de confirmación
        if (showConfirmationMessage) {
            Text(
                text = "Foto y ubicación guardada",
                color = Color.Black,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = onNavigateToUbicaciones,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Ver Lista de Ubicaciones", color = Color.White)
        }
    }
}




// Función para guardar la imagen en un Uri temporal
fun saveImageToUri(bitmap: Bitmap, context: Context): Uri {
    val filename = "captured_photo.jpg"
    val file = File(context.cacheDir, filename)
    file.outputStream().use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    return file.toUri()
}

