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
import org.osmdroid.config.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.appsandroid.clogger.viewmodel.WeatherViewModel
import com.appsandroid.clogger.utils.LocationHelper
import androidx.navigation.NavController
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.Manifest
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.overlay.TilesOverlay
import androidx.compose.material.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color

/*@Composable
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
}*/

// Evitar BuildConfig por ahora: usar constante como en RadarScreen
private const val OWM_API_KEY: String = "32011d2ec27e189821a18e166b4655f9"

@Composable
fun MapasScreen(viewModel: WeatherViewModel = WeatherViewModel(), navController: NavController? = null) {
    val context = LocalContext.current

    // Estado de la ubicación del usuario
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val userLocation = remember { mutableStateOf<GeoPoint?>(null) }
    // Referencia a la vista del mapa
    val mapViewState = remember { mutableStateOf<MapView?>(null) }
    // Referencia al marcador de usuario para una actualización eficiente
    val userMarker = remember { mutableStateOf<Marker?>(null) }
    // Estado para saber si el mapa ha sido centrado en la ubicación del usuario
    val hasCenteredOnUser = remember { mutableStateOf(false) }
    // Estado para seguir la ubicación en tiempo real
    val isFollowing = remember { mutableStateOf(false) }
    // Estados de capa
    val showPrecip = remember { mutableStateOf(false) }
    val showTemp = remember { mutableStateOf(false) }
    // Overlays creados
    val precipOverlay = remember { mutableStateOf<TilesOverlay?>(null) }
    val tempOverlay = remember { mutableStateOf<TilesOverlay?>(null) }
    // Proveedores de tiles por capa
    val precipProvider = remember { mutableStateOf<MapTileProviderBasic?>(null) }
    val tempProvider = remember { mutableStateOf<MapTileProviderBasic?>(null) }

    // Valores por defecto
    val defaultLocation = GeoPoint(12.136389, -86.251389)
    val defaultZoom = 17.0

    // Cargar la última ubicación conocida al iniciar

    // Leer estado persistido del ViewModel
    val savedCenter by viewModel.mapCenter.collectAsState(null)
    val savedZoom by viewModel.mapZoom.collectAsState(null)

    // Lanzador de permisos de ubicación
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            scope.launch {
                val helper = LocationHelper(context)
                val last = helper.getLastLocation()
                val current = last ?: helper.getCurrentLocation()
                current?.let { userLocation.value = GeoPoint(it.latitude, it.longitude) }
            }
        }
    }
    LaunchedEffect(Unit) {
        val fineGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            val helper = LocationHelper(context)
            val last = helper.getLastLocation()
            val current = last ?: helper.getCurrentLocation()
            current?.let { userLocation.value = GeoPoint(it.latitude, it.longitude) }
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Centrar el mapa en la ubicación del usuario cuando esté disponible por primera vez
    LaunchedEffect(userLocation.value) {
        val mapView = mapViewState.value
        val userLoc = userLocation.value
        if (mapView != null && userLoc != null && !hasCenteredOnUser.value) {
            mapView.controller.animateTo(userLoc)
            hasCenteredOnUser.value = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx: Context ->
                // Configuración de osmdroid
                Configuration.getInstance().userAgentValue = ctx.packageName
                Configuration.getInstance().load(
                    ctx,
                    ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
                )

                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    val initialZoom = savedZoom ?: defaultZoom
                    val initialCenter = savedCenter?.let { GeoPoint(it.first, it.second) } ?: defaultLocation
                    controller.setZoom(initialZoom)
                    controller.setCenter(initialCenter)
                    setMultiTouchControls(true)

                    // Escuchar cambios de mapa para persistir centro/zoom
                    addMapListener(object : MapListener {
                        override fun onScroll(event: ScrollEvent?): Boolean {
                            val c = this@apply.mapCenter
                            viewModel.setMapCenter(c.latitude, c.longitude)
                            return false
                        }

                        override fun onZoom(event: ZoomEvent?): Boolean {
                            viewModel.setMapZoom(zoomLevelDouble)
                            return false
                        }
                    })
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { mapView ->
                mapViewState.value = mapView

                // Actualizar la posición del marcador del usuario si la ubicación está disponible
                val userLoc = userLocation.value
                if (userLoc != null) {
                    if (userMarker.value == null) {
                        // Crear el marcador si no existe
                        val marker = Marker(mapView)
                        marker.title = "Tu ubicación"
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        // Icono del sistema para la ubicación
                        marker.icon = ContextCompat.getDrawable(mapView.context, android.R.drawable.ic_menu_mylocation)
                        mapView.overlays.add(marker)
                        userMarker.value = marker
                    }
                    // Actualizar la posición del marcador existente
                    userMarker.value?.position = userLoc
                    mapView.invalidate() // Forzar el redibujado del mapa
                }

                // Actualizar overlays meteorológicos
                val apiKey = OWM_API_KEY
                if (apiKey.isNotEmpty()) {
                    // Capturar estado actual del mapa para preservarlo
                    val currentCenter = mapView.mapCenter
                    val currentZoom = mapView.zoomLevelDouble

                    // Precipitación
                    if (showPrecip.value) {
                        if (precipOverlay.value == null) {
                            val src = object : OnlineTileSourceBase(
                                "OWM-Precip",
                                0, 19, 256, ".png",
                                arrayOf("https://tile.openweathermap.org/map/precipitation_new/")
                            ) {
                                override fun getTileURLString(pMapTileIndex: Long): String {
                                    val z = MapTileIndex.getZoom(pMapTileIndex)
                                    val x = MapTileIndex.getX(pMapTileIndex)
                                    val y = MapTileIndex.getY(pMapTileIndex)
                                    return "$baseUrl$z/$x/$y.png?appid=$apiKey"
                                }
                            }
                            val provider = MapTileProviderBasic(mapView.context)
                            provider.tileSource = src
                            val overlay = TilesOverlay(provider, mapView.context)
                            overlay.setLoadingBackgroundColor(0x00000000)
                            mapView.overlays.add(overlay)
                            precipOverlay.value = overlay
                            precipProvider.value = provider
                        } else {
                            // limpiar caché para forzar recarga si ya existía
                            precipProvider.value?.clearTileCache()
                        }
                    } else {
                        precipOverlay.value?.let { overlay ->
                            mapView.overlays.remove(overlay)
                            precipOverlay.value = null
                            precipProvider.value?.clearTileCache()
                            precipProvider.value = null
                        }
                    }

                    // Reaplicar centro/zoom y refrescar
                    mapView.controller.setCenter(currentCenter)
                    mapView.controller.setZoom(currentZoom)
                    mapView.invalidate()
                }
            }
        )

        // Manejo de ciclo de vida para MapView
        DisposableEffect(mapViewState.value) {
            val mapView = mapViewState.value
            if (mapView != null) {
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_RESUME -> mapView.onResume()
                        Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                        else -> {}
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
            } else {
                onDispose { }
            }
        }

        // Toggles de capas meteorológicas (arriba a la derecha)
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .background(Color(0xAAFFFFFF))
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            FloatingActionButton(
                onClick = { showPrecip.value = !showPrecip.value },
                modifier = Modifier.padding(end = 8.dp),
                backgroundColor = if (showPrecip.value) MaterialTheme.colors.secondary else MaterialTheme.colors.surface
            ) { Text("Lluvia") }

        }

        // Actualizaciones periódicas cuando 'seguir' está activo
        LaunchedEffect(isFollowing.value) {
            if (isFollowing.value) {
                val helper = LocationHelper(context)
                while (isActive) {
                    val fineGranted = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                    val coarseGranted = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                    if (!(fineGranted || coarseGranted)) break
                    val loc = helper.getCurrentLocation()
                    if (loc != null) {
                        val geo = GeoPoint(loc.latitude, loc.longitude)
                        userLocation.value = geo
                        mapViewState.value?.controller?.animateTo(geo)
                    }
                    delay(5000)
                }
            }
        }

        // Forzar refresco cuando cambian las capas manteniendo centro/zoom
        LaunchedEffect(showPrecip.value, showTemp.value) {
            val mapView = mapViewState.value ?: return@LaunchedEffect
            val center = mapView.mapCenter
            val targetZoom = if (showPrecip.value || showTemp.value) 8.0 else mapView.zoomLevelDouble
            precipProvider.value?.clearTileCache()
            tempProvider.value?.clearTileCache()
            mapView.controller.setCenter(center)
            mapView.controller.setZoom(targetZoom)
            mapView.invalidate()
        }

    }
}
