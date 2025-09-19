plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.appsandroid.clogger"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.appsandroid.clogger"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// 👇 Fuerza las versiones correctas de kotlin-stdlib
configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-stdlib:1.9.25")
        force("org.jetbrains.kotlin:kotlin-stdlib-common:1.9.25")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.25")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.25")
    }
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.datastore.preferences.core.android)
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.play.services.location)
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

// ==========================================================================================
//   Compose & UI
// ==========================================================================================
// Navegación en Jetpack Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

// ViewModel para Jetpack Compose (gestión de estado)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2") // versión más reciente que tenías

// ViewModel estándar con soporte de KTX
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

// Lifecycle Runtime (para observación de ciclos de vida)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

// Actividad Compose
    implementation("androidx.activity:activity-compose:1.8.0")

// Iconos extendidos de Material Design
    implementation("androidx.compose.material:material-icons-extended:1.3.0")

// Material3 Placeholder Shimmer (efecto visual mientras carga contenido)
    implementation("com.google.accompanist:accompanist-placeholder-material:0.32.0")

// Carrusel / Pager Animations
    implementation("com.google.accompanist:accompanist-pager:0.36.0")

// Indicadores para HorizontalPager
    implementation("com.google.accompanist:accompanist-pager-indicators:0.36.0")

// ==========================================================================================
// Redes & API
// ==========================================================================================

// Retrofit para consumir APIs REST
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

// Conversor Gson para Retrofit (JSON <-> Objetos)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// OkHttp cliente HTTP para Retrofit u otras peticiones
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

// ==========================================================================================
// Persistencia & Preferences
// ==========================================================================================

// DataStore Preferences (almacenamiento clave-valor)
    implementation("androidx.datastore:datastore-preferences:1.1.0")

// ==========================================================================================
// Concurrencia
// ==========================================================================================

// Coroutines para operaciones asíncronas en Android
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// ==========================================================================================
// Notificaciones
// ==========================================================================================

// Firebase Cloud Messaging (push notifications)
    implementation("com.google.firebase:firebase-messaging-ktx:23.2.0")

// ==========================================================================================
// Gráficos
// ==========================================================================================

// MPAndroidChart (gráficos de barras, líneas, pastel, etc.)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

// OSMDroid (mapas y gráficos satelitales)
    implementation("org.osmdroid:osmdroid-android:6.1.16")

// Capas WMS adicionales (por ejemplo meteorología sobre mapas)
    implementation("org.osmdroid:osmdroid-wms:6.1.16")

// ==========================================================================================
// PDF
// ==========================================================================================

// iText7 para generación de PDFs
    implementation("com.itextpdf:itext7-core:8.0.3")

// ==========================================================================================
// Archivos CSV
// ==========================================================================================

// OpenCSV para leer/escribir archivos CSV
    implementation("com.opencsv:opencsv:5.7.1")

// ==========================================================================================
// Tareas en segundo plano
// ==========================================================================================

// WorkManager para tareas programadas o periódicas
    implementation("androidx.work:work-runtime-ktx:2.8.1")
}
// ==========================================================================================