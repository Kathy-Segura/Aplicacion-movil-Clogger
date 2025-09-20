package com.appsandroid.clogger.ui.theme.screen

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapasScreen() {
    /*AndroidView(factory = { context: Context ->
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            controller.setZoom(7.0)
            controller.setCenter(GeoPoint(12.8654, -85.2072))
            setMultiTouchControls(true)

            val marker = Marker(this)
            marker.position = GeoPoint(12.136389, -86.251389)
            marker.title = "Managua"
            overlays.add(marker)
        }
    }, modifier = Modifier.fillMaxSize())*/
}
