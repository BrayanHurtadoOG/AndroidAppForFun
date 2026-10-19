# 🔥 Uchiha Player — Reproductor de Música Android

> Rama activa de desarrollo: `kurama/uchiha-player` (temática del clan Uchiha + funciones completas del reproductor).

## 📋 Descripción General
**Uchiha Player** (antes "Example") es una aplicación de reproducción de música local construida con las últimas tecnologías de Android: **Jetpack Compose**, **Media3 (ExoPlayer)**, **ViewModel** y arquitectura moderna. La app permite:
- Escanear y listar todas las canciones almacenadas en el dispositivo
- Reproducir/pausar canciones desde una interfaz moderna
- Reproducción en segundo plano mediante un Foreground Service
- Cola de reproducción real: al terminar una canción avanza sola a la siguiente
- Controles de reproducción persistentes en la barra inferior + seekbar arrastrable
- Shuffle y modos de repetición (off / all / one)
- Búsqueda/filtrado de canciones por título o artista
- Pantalla completa "Now Playing" con carátula grande y slider de volumen
- Carátulas reales desde MediaStore (con fallback al Sharingan)
- Persistencia con DataStore: recuerda la última canción y la posición
- Notificación de reproducción con arte Uchiha
- Tema oscuro fijo del clan Uchiha (negros profundos + rojo Sharingan)

---

## 🏗️ Arquitectura del Proyecto

```
UchihaPlayer/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/example/
│   │   │   │   ├── data/
│   │   │   │   │   ├── MediaRepository.kt          # Repositorio: acceso a MediaStore (+ albumId)
│   │   │   │   │   └── UserPreferences.kt          # DataStore: última canción + posición
│   │   │   │   ├── ui/
│   │   │   │   │   ├── components/
│   │   │   │   │   │   └── AlbumArt.kt             # Carátula desde MediaStore (fallback Sharingan)
│   │   │   │   │   ├── theme/
│   │   │   │   │   │   ├── Color.kt                # Paleta Uchiha (negro + rojo Sharingan)
│   │   │   │   │   │   ├── Theme.kt                # Tema oscuro fijo del clan (sin dynamic color)
│   │   │   │   │   │   └── Type.kt                 # Tipografía
│   │   │   │   │   ├── MusicPlayerScreen.kt        # UI Compose: lista + buscador + seekbar
│   │   │   │   │   ├── NowPlayingScreen.kt         # UI Compose: pantalla completa Now Playing
│   │   │   │   │   └── MusicViewModel.kt           # ViewModel: cola real, shuffle/repeat, volumen
│   │   │   │   ├── MainActivity.kt                 # Activity principal (entry point)
│   │   │   │   └── MusicService.kt                 # Foreground Service + notificación Uchiha
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   │   ├── ic_uchiha_fan.xml           # Logo del clan: abanico (mitad blanca/roja)
│   │   │   │   │   ├── itachi.jpg / itachimange.jpg# Arte de fondo (Itachi / Mangekyou)
│   │   │   │   │   └── sharingan.jpg               # Fallback de carátula
│   │   │   │   └── ...                             # Recursos (valores, íconos, temas)
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
| **View** | Mostrar datos y reaccionar a eventos del usuario | `MainActivity`, `MusicPlayerScreen` (Compose) |
| **ViewModel** | Mantener estado, lógica de UI, comunicación con la capa de datos | `MusicViewModel` |
| **Model/Repository** | Acceso a datos (MediaStore), fuente única de verdad | `MediaRepository` |
| **Service** | Reproducción en segundo plano | `MusicService` |

---

## 🛠️ Stack Tecnológico

### Lenguaje
- **Kotlin 2.2.10**

### UI
- **Jetpack Compose 2026.02.01 (BOM)** - Toolkit declarativo moderno
- **Material3** - Sistema de diseño de Google
- **Scaffold, LazyColumn, Card, IconButton** - Componentes Compose
- **Tema oscuro fijo del clan Uchiha** - Paleta propia (negro #0A0A0C + rojo Sharingan #E53935), dynamic color desactivado a propósito

### Arquitectura y Ciclo de Vida
- **ViewModel** + **StateFlow** - Gestión del estado sobreviviendo a cambios de configuración
- **viewModelScope** - Corrutinas ligadas al ciclo de vida del ViewModel
- **ViewModelProvider.Factory** - Inyección manual de dependencias

### Reproducción de Audio
- **Media3 1.10.1** (anteriormente ExoPlayer)
  - `ExoPlayer` - Motor de reproducción
  - `MediaSession` / `MediaSessionService` - Sesión de media para control externo
  - `MediaController` - Controlador desde la UI
- **Foreground Service** - `FOREGROUND_SERVICE_MEDIA_PLAYBACK` para Android 14+

### Acceso a Datos
- **MediaStore** - Proveedor de contenido para acceder a archivos de audio locales
- **ContentResolver** / **Cursor** - Consulta de la base de datos de medios

### Permisos
- `READ_MEDIA_AUDIO` (Android 13+) - Acceso a archivos de audio
- `READ_EXTERNAL_STORAGE` (hasta API 32) - Retrocompatibilidad
- `FOREGROUND_SERVICE` + `FOREGROUND_SERVICE_MEDIA_PLAYBACK` - Reproducción en segundo plano

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
```
- Declara **permisos runtime** (no se conceden automáticamente)
- Declara `MainActivity` como launcher (punto de entrada)
- Declara `MusicService` como `MediaSessionService` con `foregroundServiceType="mediaPlayback"`

### 2. `MainActivity.kt`
- Extiende **`ComponentActivity`** (base para Compose)
- `enableEdgeToEdge()` - Modo edge-to-edge (contenido bajo barras de estado/navegación)
- `setContent { }` - Establece el árbol Compose como contenido de la Activity
- Maneja **solicitud de permisos runtime** con `rememberLauncherForActivityResult`
- Crea `MediaRepository` y `MusicViewModel` con Factory personalizada
- `LaunchedEffect(Unit)` - Efecto de composición que se ejecuta una sola vez

### 3. `MusicViewModel.kt`
```kotlin
private val _songs = MutableStateFlow<List<Song>>(emptyList())
val songs: StateFlow<List<Song>> = _songs
```
- **StateFlow** (flow frío) para la lista de canciones (observable desde Compose)
- **mutableStateOf** para `currentSong` e `isPlaying` (estado de Compose)
- `initController()`: Conecta con `MediaSession` del Service mediante `MediaController`
- Escucha eventos del Player con `Player.Listener` (cambios de reproducción, transiciones de canción)
- `onCleared()`: Libera recursos del `MediaController`

### 4. `MusicService.kt`
```kotlin
class MusicService : MediaSessionService()
```
- Extiende **`MediaSessionService`** (clase de Media3 que implementa `Service`)
- En `onCreate()`: Crea `ExoPlayer` y lo envuelve en `MediaSession`
- `onGetSession()`: Expone la sesión a los controladores (UI, notificaciones, Wear OS, Auto)
- `onDestroy()`: Libera el player y la sesión (evita fugas de memoria)
- El Service se ejecuta en **foreground** para que Android no lo mate

### 5. `MediaRepository.kt`
```kotlin
context.contentResolver.query(
    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
    projection, selection, null, sortOrder
)?.use { cursor -> ... }
```
- **Repository Pattern**: La UI no sabe de dónde vienen los datos
- Consulta **`MediaStore`** (ContentProvider del sistema) usando `ContentResolver`
- `projection`: Columnas a extraer (ID, título, artista, duración)
- `selection`: Filtra solo archivos de música (`IS_MUSIC != 0`)
- `.use { }` - Cierra el Cursor automáticamente (evita fugas)
- `ContentUris.withAppendedId()` - Construye la `Uri` de cada canción

### 6. `MusicPlayerScreen.kt` (Compose)
- **`Scaffold`** - Estructura estándar de Material3 (topBar, content, bottomBar)
- **`bottomBar`**: Muestra `PlaybackControls` SOLO si hay una canción actual
- **`LazyColumn`**: Lista eficiente (solo compone lo visible, como RecyclerView)
- **`collectAsState()`**: Convierte `StateFlow` a `State` de Compose (recompone cuando cambia)
- **`SongItem`**: Cada canción es una `Card` clickeable con `modifier.clickable`
- **Resalte visual**: La canción seleccionada usa `primaryContainer` como color de fondo
- **Iconos Material**: `Icons.Default.PlayArrow` / `Pause`

---

## 🚀 Cómo Ejecutar el Proyecto

### Prerrequisitos
1. **Android Studio** (versión reciente compatible con AGP 9.3.1)
2. **JDK 17+** (incluido en Android Studio)
3. Dispositivo Android con **API 34 (Android 14) o superior**, o emulador
4. Archivos de música almacenados en el dispositivo

### Pasos
1. Clonar/abrir el proyecto en Android Studio
2. Esperar a que Gradle sincronice las dependencias
3. Conectar un dispositivo o iniciar un emulador (API ≥ 34)
4. Click en **Run** (▶) o usar `./gradlew.bat installDebug`
5. Al abrir la app por primera vez:
   - Aparecerá el botón **"Conceder Permiso para Música"**
   - Aceptar el permiso de almacenamiento/audio
   - La app escaneará y mostrará todas las canciones locales
   - Click en una canción para reproducirla

---

## 🧪 Testing
```bash
# Tests unitarios (JVM local)
./gradlew.bat testDebugUnitTest

# Tests instrumentados (requiere dispositivo/emulador)
./gradlew.bat connectedDebugAndroidTest
```

---

## 📚 Conceptos de Android Aplicados (Referencia Rápida)
1. **Activity** + **Ciclo de vida** (`onCreate`)
2. **Jetpack Compose** - UI declarativa, recomposición, `@Composable`, `remember`, `LaunchedEffect`
3. **ViewModel** - Persistencia de estado, `viewModelScope`
4. **Repository Pattern** - Separación de fuentes de datos
5. **StateFlow** - Flujo observable para estado
6. **Service** (Foreground Service) - Tareas de larga duración en background
7. **Media3 / ExoPlayer** - Reproducción de audio/video
8. **MediaSession** / **MediaController** - Arquitectura cliente-servidor para media
9. **ContentResolver** / **MediaStore** - Acceso a archivos del sistema
10. **Runtime Permissions** - Permisos que el usuario debe aprobar en ejecución
11. **Gradle Kotlin DSL** - Scripts de build en Kotlin
12. **Version Catalog** - Gestión centralizada de dependencias

---

## 💡 Mejoras Sugeridas para Expandir el Proyecto
- [x] Barra de progreso/seeking en los controles (seekbar arrastrable en cápsula y Now Playing)
- [x] Botones Siguiente/Anterior (delegan en el player; respetan shuffle/repeat)
- [x] Notificación de reproducción personalizada (`DefaultMediaNotificationProvider` + arte Uchiha)
- [x] Shuffle / Repeat modes (OFF → ALL → ONE, aplicados al ExoPlayer)
- [x] Persistencia de la última canción reproducida (DataStore: canción + posición)
- [x] Diapositiva para controlar volumen (slider en Now Playing)
- [x] Búsqueda/filtrado de canciones (por título o artista)
- [x] Playlists (con Room Database) — `PlaylistEntity`/`PlaylistSongEntity`, `PlaylistDao`, `UchihaDatabase`, pestaña Playlists en la UI con crear/borrar/añadir/quitar/reproducir
- [x] Pantalla de "Now Playing" a pantalla completa (fondo Mangekyou de Itachi)
- [x] Miniatura de álbum (carátula) desde MediaStore (fallback al Sharingan)
- [ ] Coil para carga de imágenes — pendiente (hoy se usa `loadThumbnail` + `painterResource`)
- [ ] Hilt para inyección de dependencias (reemplazar Factory manual) — pendiente
- [ ] Navigation Compose para múltiples pantallas — pendiente (hoy navegación por estado)

## 🔥 Temática del clan Uchiha
- **Paleta** (`ui/theme/`): `UchihaBlack #0A0A0C`, superficies oscuras, `SharinganRed #E53935` como primario, `AkatsukiRed`/`SharinganDarkRed` como acentos. Tema siempre oscuro, sin dynamic color.
- **Logo** (`res/drawable/ic_uchiha_fan.xml`): vector del abanico del clan Uchiha — círculo con mitad superior blanca e inferior roja, mango gris. Usado como ícono de la app (adaptive icon normal y round), logo en Now Playing y arte de fallback en notificaciones (`android.resource://` URI en el `MediaMetadata`).
- **Arte**: `itachi.jpg` como fondo de la pantalla principal, `itachimange.jpg` como fondo de Now Playing, `sharingan.jpg` como fallback de carátulas.

## ⚠️ Nota de compilación
Este código se escribió y revisó sin Android SDK/Java disponibles en el entorno de desarrollo (no se pudo compilar ahí). Antes de instalar en un dispositivo, abrir el proyecto en **Android Studio**, sincronizar Gradle y compilar (`./gradlew installDebug`). Revisar el Logcat en la primera ejecución por si alguna API necesita ajuste fino.
