# 🍥 Anime Music Player - Reproductor de Música con Temática Anime

## 📋 Descripción General
**Anime Music Player** (antes "Example") es una aplicación de reproducción de música local con una estética inspirada en el anime, construida con las últimas tecnologías de Android: **Jetpack Compose**, **Media3 (ExoPlayer)**, **ViewModel** y arquitectura moderna MVVM. La app combina funcionalidad de reproductor profesional con un diseño visual inmersivo.

### ✨ Características Principales
- 🎵 **Escaneo inteligente** de todas las canciones almacenadas en el dispositivo mediante MediaStore
- ▶️ **Reproducción/Pausa** con controles responsive y animados
- ⏭️ **Navegación entre pistas**: Botones Siguiente (SkipNext) y Anterior (SkipPrevious)
- 🔀 **Modo Aleatorio (Shuffle)** con indicador visual activo (color verde Spotify)
- 📱 **Reproducción en segundo plano** mediante Foreground Service + MediaSession
- 💎 **Interfaz Glassmorphism**: Barra de controles flotante en formato cápsula con efecto cristal esmerilado
- 🎨 **Temática Anime inmersiva**: Fondos de pantalla de personajes, miniaturas temáticas y paleta de colores oscura
- 🎚️ **Cabecera tipo Spotify**: Portada de playlist con estadísticas, botón Play circular grande y barra de acciones
- 🔔 **Notificaciones del sistema**: Integración nativa con controles de media (a través de MediaSession)
- 🌓 **Soporte modo edge-to-edge**: Contenido bajo barras de estado y navegación

---

## 🎨 Temática Anime y Recursos Visuales

La aplicación cuenta con una identidad visual completa inspirada en el anime:

| Recurso | Archivo | Uso |
|---------|---------|-----|
| 🌑 **Fondo de pantalla principal** | `itachi.jpg` | Imagen de fondo a pantalla completa (Itachi Uchiha - Naruto) |
| 🖼️ **Portada de Playlist** | `itachimange.jpg` | Imagen destacada en la cabecera (Mangekyo Sharingan) |
| 👁️ **Miniaturas de canciones** | `sharingan.jpg` | Icono en cada item de la lista y controles flotantes |
| 🐉 **Recurso adicional** | `sindragosa.jpg` | Disponible para futuras expansiones (WoW) |

### Paleta de Colores
| Elemento | Color | Código |
|----------|-------|--------|
| Acento principal (Play, Shuffle activo) | Verde Spotify | `#1DB954` |
| Fondos de UI | Negro semi-transparente | `Color.Black.copy(alpha = 0.85f)` |
| Textos primarios | Blanco puro | `Color.White` |
| Textos secundarios | Blanco difuminado | `Color.White.copy(alpha = 0.6f - 0.7f)` |
| Item seleccionado | Blanco semitransparente | `Color.White.copy(alpha = 0.15f)` |
| Canción reproduciéndose | Verde Spotify | `#1DB954` |

---

## 🏗️ Arquitectura del Proyecto

```
Example/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/example/
│   │   │   │   ├── data/
│   │   │   │   │   └── MediaRepository.kt          # Repositorio: acceso a MediaStore + modelo Song
│   │   │   │   ├── ui/
│   │   │   │   │   ├── theme/
│   │   │   │   │   │   ├── Color.kt                # Definición de colores (Material3)
│   │   │   │   │   │   ├── Theme.kt                # Configuración del tema Material3
│   │   │   │   │   │   └── Type.kt                 # Tipografía
│   │   │   │   │   ├── ExampleApp.kt               # 🆕 Composable raíz: permisos, fondo, navegación
│   │   │   │   │   ├── MusicPlayerScreen.kt        # UI Compose: pantalla completa del reproductor
│   │   │   │   │   └── MusicViewModel.kt           # ViewModel: estado + lógica de reproducción
│   │   │   │   ├── MainActivity.kt                 # Activity principal (entry point minimalista)
│   │   │   │   └── MusicService.kt                 # Foreground Service de reproducción (MediaSessionService)
│   │   │   ├── res/
│   │   │   │   ├── drawable/                       # 🆕 Imágenes temáticas anime
│   │   │   │   │   ├── itachi.jpg                  # Fondo principal
│   │   │   │   │   ├── itachimange.jpg             # Portada de playlist
│   │   │   │   │   ├── sharingan.jpg               # Miniaturas
│   │   │   │   │   └── sindragosa.jpg              # Recurso adicional
│   │   │   │   ├── values/
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   └── mipmap-*/                       # Iconos launcher adaptive
│   │   │   └── AndroidManifest.xml                 # Manifiesto de la app
│   │   ├── androidTest/                            # Tests instrumentados
│   │   └── test/                                   # Tests unitarios
│   └── build.gradle.kts                            # Configuración Gradle del módulo app
├── gradle/
│   └── libs.versions.toml                          # Version Catalog (dependencias centralizadas)
├── build.gradle.kts                                # Configuración Gradle del proyecto
├── settings.gradle.kts                             # Configuración de módulos
└── gradle.properties                               # Propiedades de Gradle
```

### Patrón Arquitectónico: MVVM (Model-View-ViewModel)
| Capa | Responsabilidad | Clases |
|------|----------------|--------|
| **View** | Mostrar datos y reaccionar a eventos del usuario | `MainActivity`, `ExampleApp`, `MusicPlayerScreen` (Compose) |
| **ViewModel** | Mantener estado, lógica de UI, comunicación con la capa de datos y control del Player | `MusicViewModel` |
| **Model/Repository** | Acceso a datos (MediaStore), fuente única de verdad | `MediaRepository`, `Song` (data class) |
| **Service** | Reproducción en segundo plano con MediaSession | `MusicService` |

---

## 🛠️ Stack Tecnológico

### Lenguaje
- **Kotlin 2.2.10**

### UI
- **Jetpack Compose 2026.02.01 (BOM)** - Toolkit declarativo moderno
- **Material3** - Sistema de diseño de Google
- **Componentes principales**: `Scaffold`, `LazyColumn`, `Card`, `IconButton`, `Surface`, `Box`, `Row`, `Column`
- **Scaffold transparente**: `containerColor = Color.Transparent` para mostrar el fondo anime
- **Efectos visuales**: `Glassmorphism` con superficies semi-transparentes y `tonalElevation`

### Arquitectura y Ciclo de Vida
- **ViewModel** + **StateFlow** - Gestión del estado sobreviviendo a cambios de configuración
- **mutableStateOf** - Estado observable de Compose para `currentSong`, `isPlaying`, `isShuffleEnabled`
- **viewModelScope** - Corrutinas ligadas al ciclo de vida del ViewModel
- **ViewModelProvider.Factory** - Inyección manual de dependencias

### Reproducción de Audio
- **Media3 1.10.1** (anteriormente ExoPlayer)
  - `ExoPlayer` - Motor de reproducción de alto rendimiento
  - `MediaSession` / `MediaSessionService` - Sesión de media para control externo (notificaciones, Wear OS, Android Auto)
  - `MediaController` - Controlador desde la UI (patrón cliente-servidor)
- **Foreground Service** - `FOREGROUND_SERVICE_MEDIA_PLAYBACK` para Android 14+
- **Player.Listener** - Escucha eventos: `onIsPlayingChanged`, `onMediaItemTransition`

### Acceso a Datos
- **MediaStore** - Proveedor de contenido del sistema para acceder a archivos de audio locales
- **ContentResolver** / **Cursor** - Consulta eficiente de la base de datos de medios
- **Auto-cierre de Cursor**: `.use { }` block para evitar fugas de memoria

### Permisos
- `READ_MEDIA_AUDIO` (Android 13+) - Acceso a archivos de audio
- `READ_EXTERNAL_STORAGE` (hasta API 32) - Retrocompatibilidad
- `FOREGROUND_SERVICE` + `FOREGROUND_SERVICE_MEDIA_PLAYBACK` - Reproducción en segundo plano
- `MEDIA_BUTTON` - Respuesta a botones físicos de auriculares

### Build System
- **Gradle 8+** con **AGP 9.3.1** (Android Gradle Plugin)
- **Kotlin DSL** (`build.gradle.kts`) en lugar de Groovy
- **Version Catalog** (`libs.versions.toml`) - Gestión centralizada de versiones

### Testing
- **JUnit 4** - Tests unitarios (locales)
- **AndroidX Test** + **Espresso** - Tests instrumentados (en dispositivo)
- **Compose UI Test** - Pruebas de UI Compose

---

## 📱 Requisitos del Proyecto
| Parámetro | Valor |
|-----------|-------|
| `minSdk` | **34** (Android 14) |
| `targetSdk` | **36** (Android 16 Preview) |
| `compileSdk` | **36** |
| `applicationId` | `com.example.example` |
| `versionCode` | 1 |
| `versionName` | 1.0 |
| Java Compatibility | **Java 11** |

---

## 🔍 Explicación Detallada de los Componentes Clave

### 1. `AndroidManifest.xml`
```xml
<uses-permission android:name="android.permission.READ_MEDIA_AUDIO" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK" />

<service
    android:name=".MusicService"
    android:foregroundServiceType="mediaPlayback">
    <intent-filter>
        <action android:name="androidx.media3.session.MediaSessionService" />
        <action android:name="android.intent.action.MEDIA_BUTTON" />
    </intent-filter>
</service>
```
- Declara **permisos runtime** (no se conceden automáticamente)
- Declara `MainActivity` como launcher (punto de entrada)
- Declara `MusicService` como `MediaSessionService` con `foregroundServiceType="mediaPlayback"`
- Filtro `MEDIA_BUTTON` para responder a botones de auriculares Bluetooth

### 2. `MainActivity.kt`
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate()
        enableEdgeToEdge()
        setContent {
            ExampleTheme {
                ExampleApp()
            }
        }
    }
}
```
- Extiende **`ComponentActivity`** (base recomendada para Compose)
- `enableEdgeToEdge()` - Modo edge-to-edge (contenido bajo barras de estado/navegación)
- **Entry point minimalista**: Delega toda la lógica de UI a `ExampleApp()`
- Aplica `ExampleTheme` (Material3) a toda la jerarquía Compose

### 3. `ExampleApp.kt` 🆕
```kotlin
@Composable
fun ExampleApp() {
    val context = LocalContext.current
    val repository = remember { MediaRepository(context) }
    val viewModel: MusicViewModel = viewModel(factory = ...)
    
    var permissionGranted by remember { mutableStateOf(...) }
    val launcher = rememberLauncherForActivityResult(...)
    
    LaunchedEffect(Unit) {
        viewModel.initController(context)
        if (permissionGranted) viewModel.loadSongs()
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo anime (Itachi)
        Image(painter = painterResource(id = R.drawable.itachi), ...)
        
        if (permissionGranted) {
            MusicPlayerScreen(viewModel)
        } else {
            // Botón central para conceder permiso
            Button(onClick = { launcher.launch(Manifest.permission.READ_MEDIA_AUDIO) }) {
                Text("Conceder Permiso para Música")
            }
        }
    }
}
```
- **Composable raíz** de la aplicación
- **Solicitud de permisos runtime** con `rememberLauncherForActivityResult` (RequestPermission)
- **Inicialización diferida**: `LaunchedEffect(Unit)` ejecuta una sola vez
- **Patrón de fondo**: `Box` apila `Image` de fondo + contenido interactivo
- **Dependency Injection manual**: Factory para pasar `MediaRepository` al `MusicViewModel`

### 4. `MusicViewModel.kt`
```kotlin
class MusicViewModel(private val repository: MediaRepository) : ViewModel() {
    
    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs
    
    private val _currentSong = mutableStateOf<Song?>(null)
    private val _isPlaying = mutableStateOf(false)
    private val _isShuffleEnabled = mutableStateOf(false)  // 🆕 Estado Shuffle
    
    // 🆕 Navegación entre pistas con lógica de Shuffle
    fun playNext() {
        if (_isShuffleEnabled.value) {
            playSong(currentList[(0 until currentList.size).random()])
        } else {
            val nextIndex = currentIndex + 1
            if (nextIndex < currentList.size) playSong(currentList[nextIndex])
        }
    }
    
    fun playPrevious() { /* Similar con index - 1 o random */ }
    fun toggleShuffle() { _isShuffleEnabled.value = !_isShuffleEnabled.value }
}
```
- **StateFlow** para la lista de canciones (observable reactivo)
- **mutableStateOf** para estado de Compose con ciclo de vida optimizado
- **MediaController** se conecta al `MusicService` mediante `SessionToken`
- **Player.Listener**: Actualiza UI cuando cambia `isPlaying` o transiciona de canción
- **Lógica Shuffle**: `Random` en `playNext()` / `playPrevious()` cuando está activado
- `onCleared()`: Libera `MediaController` con `MediaController.releaseFuture()`

### 5. `MusicService.kt`
```kotlin
class MusicService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = 
        mediaSession
}
```
- Extiende **`MediaSessionService`** (clase helper de Media3)
- **Ciclo de vida Service**: `onCreate` → `onGetSession` (múltiples clientes) → `onDestroy`
- `ExoPlayer` + `MediaSession` se crean una sola vez y se reutilizan
- El Service se ejecuta en **foreground** automáticamente cuando hay reproducción
- Android Auto, Wear OS y Notificaciones se conectan a través de `onGetSession()`

### 6. `MediaRepository.kt`
```kotlin
data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val uri: Uri,
    val duration: Int
)

class MediaRepository(private val context: Context) {
    fun fetchLocalSongs(): List<Song> {
        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection = arrayOf(_ID, TITLE, ARTIST, DURATION),
            selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0",
            sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        )?.use { cursor ->
            // Extraer columnas + iterar cursor
        }
    }
}
```
- **Repository Pattern**: La UI no conoce la fuente de datos
- **`IS_MUSIC != 0`**: Filtra tonos de llamada, grabaciones y archivos no musicales
- **`ContentUris.withAppendedId()`**: Construye `Uri` válida para cada pista
- `.use { }` - Cierra el `Cursor` automáticamente (previene fugas)
- **Orden alfabético**: `TITLE ASC` para lista consistente

### 7. `MusicPlayerScreen.kt` (Compose) 🆕 Diseño rediseñado
```kotlin
@Composable
fun MusicPlayerScreen(viewModel: MusicViewModel) {
    Scaffold(
        containerColor = Color.Transparent,  // Fondo anime visible
        bottomBar = { PlaybackControls(...) } // Cápsula flotante glassmorphism
    ) { padding ->
        LazyColumn {
            item { PlaylistHeader() }        // Cabecera tipo Spotify
            item { ActionButtonsBar(...) }   // Shuffle + Play grande
            items(songs) { SongItem(...) }   // Lista de canciones
        }
    }
}
```

#### `PlaylistHeader()` - Cabecera moderna
- Fila superior con botones: Salir, Favoritos, Añadir, Más opciones
- **Card de portada**: 200.dp, `R.drawable.itachimange`, elevación 12.dp
- Título "Todas las Canciones" + contador "109 pistas"

#### `ActionButtonsBar()` - Barra de acciones
- Icono de orden: "Mi orden"
- 🆕 **Botón Shuffle**: Icono se vuelve `#1DB954` (verde Spotify) cuando está activo
- 🆕 **Botón Play circular grande**: 56.dp, color verde, icono negro

#### `SongItem()` - Item de lista
- Formato tarjeta con bordes redondeados (12.dp)
- Fondo semi-transparente: `alpha = 0.15f` cuando está seleccionado
- Miniatura `sharingan.jpg` en Card de 50.dp
- Título en **verde** si está reproduciéndose, blanco si no
- Artista en blanco difuminado (alpha 0.6f)

#### `PlaybackControls()` - Barra flotante Glassmorphism 🆕
```kotlin
Surface(
    modifier = Modifier.fillMaxWidth().height(64.dp),
    shape = RoundedCornerShape(32.dp),     // Forma de cápsula
    color = Color.Black.copy(alpha = 0.85f) // Cristal esmerilado
) {
    Row {
        Card(shape = CircleShape) { /* Miniatura sharingan */ }
        Column { /* Título + Artista */ }
        Row {
            IconButton(SkipPrevious)
            IconButton(PlayPause)    // 32.dp central
            IconButton(SkipNext)
            IconButton(Cola/Menu)
        }
    }
}
```
- **Diseño cápsula flotante**: Separación `bottom = 47.dp` del borde
- **Glassmorphism**: `Color.Black.copy(alpha = 0.85f)` + `tonalElevation = 8.dp`
- 🆕 **Botones SkipPrevious / SkipNext** totalmente funcionales
- Cuarto botón de "Cola" preparado para futura expansión

---

## 🚀 Cómo Ejecutar el Proyecto

### Prerrequisitos
1. **Android Studio** (versión reciente compatible con AGP 9.3.1)
2. **JDK 17+** (incluido en Android Studio)
3. Dispositivo Android con **API 34 (Android 14) o superior**, o emulador
4. Archivos de música almacenados en el almacenamiento interno del dispositivo

### Pasos
1. Clonar/abrir el proyecto en Android Studio
2. Esperar a que Gradle sincronice las dependencias (descarga Media3, Compose BOM, etc.)
3. Conectar un dispositivo físico o iniciar un emulador (API ≥ 34)
4. Click en **Run** (▶) o ejecutar en terminal:
   ```bash
   ./gradlew.bat installDebug
   ```
5. **Primera ejecución**:
   - Aparecerá el botón **"Conceder Permiso para Música"** sobre el fondo de Itachi
   - Aceptar el permiso `READ_MEDIA_AUDIO` en el diálogo del sistema
   - La app escaneará automáticamente y mostrará todas las canciones locales
   - Tocar cualquier canción comenzará la reproducción
   - El reproductor flotante aparecerá automáticamente en la parte inferior

### Controles Rápidos
| Acción | Ubicación |
|--------|-----------|
| Reproducir/Pausar | Barra flotante central o Play grande de cabecera |
| Siguiente canción | Botón ⏭️ en barra flotante |
| Anterior canción | Botón ⏮️ en barra flotante |
| Modo aleatorio | Botón 🔀 en barra de acciones (verde = activado) |
| Cambiar de pista | Tocar directamente cualquier item de la lista |

---

## 🧪 Testing
```bash
# Tests unitarios (JVM local - no requiere dispositivo)
./gradlew.bat testDebugUnitTest

# Tests instrumentados (requiere dispositivo físico o emulador conectado)
./gradlew.bat connectedDebugAndroidTest
```

---

## 📚 Conceptos de Android Aplicados (Referencia Rápida)
1. **Activity** + **Ciclo de vida** (`onCreate`) + **enableEdgeToEdge**
2. **Jetpack Compose** - UI declarativa, recomposición, `@Composable`, `remember`, `mutableStateOf`, `LaunchedEffect`
3. **ViewModel** - Persistencia de estado a rotaciones, `viewModelScope`
4. **Repository Pattern** - Separación de fuentes de datos (MediaStore desacoplado)
5. **StateFlow** / **Flow** - Flujo observable reactivo para estado
6. **Service** (Foreground Service) - Tareas de larga duración en background
7. **Media3 / ExoPlayer** - Reproducción de audio de alto rendimiento
8. **MediaSession** / **MediaController** - Arquitectura cliente-servidor para media
9. **ContentResolver** / **MediaStore** - Acceso a archivos multimedia del sistema
10. **Runtime Permissions** - Permisos con `rememberLauncherForActivityResult`
11. **Gradle Kotlin DSL** - Scripts de build en Kotlin
12. **Version Catalog** (`libs.versions.toml`) - Gestión centralizada de dependencias
13. **Glassmorphism Design** - UI con transparencias y elevación tonal
14. **Edge-to-Edge** - Contenido bajo System Bars (status/navigation)

---

## ✅ Características Implementadas (Progreso)
- [x] Escaneo y listado de canciones locales
- [x] Reproducir / Pausar
- [x] Botones **Siguiente / Anterior**
- [x] **Modo Aleatorio (Shuffle)** con indicador visual
- [x] Reproducción en segundo plano (Foreground Service)
- [x] Controles persistentes de reproducción
- [x] Tema dinámico Material You + tema personalizado oscuro
- [x] **Fondo de pantalla temático anime**
- [x] **Cabecera tipo Spotify** (portada + estadísticas + acciones)
- [x] **Glassmorphism** en barra de controles flotante
- [x] Miniaturas temáticas por canción
- [x] Resalte visual de canción reproduciéndose
- [x] Edge-to-Edge display
- [x] MediaSession para notificaciones y controles externos

## 💡 Mejoras Sugeridas para Expandir el Proyecto
- [ ] **Barra de progreso** con seeking (seekbar táctil)
- [ ] **Modos Repeat**: Ninguno, Todas, Una
- [ ] **Notificación personalizada** con arte de álbum grande
- [ ] **Persistencia** de última canción (DataStore Preferences)
- [ ] **Control de volumen** mediante deslizamiento táctil
- [ ] **Búsqueda** y filtrado de canciones en tiempo real
- [ ] **Playlists** personalizadas con **Room Database**
- [ ] **Pantalla Now Playing** a pantalla completa (animación de vinilo/portada)
- [ ] **Extraer carátulas de álbum** desde MediaStore (columna ALBUM_ART)
- [ ] **Coil** para carga asíncrona de imágenes y memory caching
- [ ] **Hilt** para inyección de dependencias (reemplazar Factory manual)
- [ ] **Navigation Compose** para múltiples pantallas (Now Playing, Playlists, Settings)
- [ ] **Ecualizador** integrado (frecuencias, preajustes)
- [ ] **Timer de apagado** (Sleep Timer)
- [ ] **Letras de canciones** sincronizadas (LRC format)
- [ ] **Más temas anime**: Cambiador de fondos entre varios personajes
