package com.example.lab12plataformasmoviles

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter

// Modelo de datos para ubicación y foto
data class LocationPhoto(val latitude: String, val longitude: String, val imageUri: Uri?)

@Composable
fun UbicacionesScreen(locations: List<LocationPhoto>, modifier: Modifier = Modifier.fillMaxSize()) {
    LazyColumn(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(locations) { locationPhoto ->
            LocationCard(locationPhoto)
        }
    }
}

@Composable
fun LocationCard(locationPhoto: LocationPhoto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Latitud: ${locationPhoto.latitude}")
            Text(text = "Longitud: ${locationPhoto.longitude}")
            locationPhoto.imageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Imagen del lugar",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
