# Agents Changelogs — Uchiha Player

Archivo de coordinación entre los agentes que trabajan en este repo.
Cada agente registra aquí sus cambios **antes** de tocar código ajeno y **al terminar** su tarea.

## Agentes activos en este repo

- **Kurama** — núcleo, datos e integración
- **Kaguya** — capa visual / UI

(Sesshomaru no participa en este repo por el momento.)

## División de tareas

### Kurama — núcleo, datos e integración

- Motor de reproducción: Media3/ExoPlayer, servicio de reproducción, notificación personalizada, cola.
- Capa de datos: Room, DataStore, repositorios.
- Migración a Hilt (es transversal: toca casi todos los archivos).
- Revisión e integración de PRs.
- Tests de su capa (datos / núcleo).

### Kaguya — capa visual / UI

- Pantallas Compose, temática Uchiha, animaciones.
- Migración a Coil (carátulas).
- Migración a Navigation Compose.
- Widget de escritorio (si se aprueba).
- Tests de su capa (UI).

## Reglas de coordinación

1. **Un dueño por módulo.** Nadie toca archivos del área de otro sin registrarlo aquí antes: la entrada funciona como reserva y evita conflictos de merge.
2. **Una rama por agente:** `kurama/<tarea>`, `kaguya/<tarea>`. Todo entra a `master` por PR.
3. **Entradas cortas con la plantilla fija.** Para ponerse al día se leen solo las últimas entradas; las viejas se resumen en la sección **Archivo**.
4. **Tareas con contrato breve:** qué archivos toca, qué debe lograr y qué NO debe tocar.
5. Cada agente testea su propia capa.
6. Shinku hará correcciones en los archivos y configuraciones que no queden como espera; por ende, Kurama debe revisar la rama `Shinku-review`. (Corregido por Shinku)
7. Cada vez que se haga un commit en la rama `Shinku-review`, Kurama actualizará el Agents.changelogs.md y luego cada agente trabajará en lo que haya que arreglar. (Corregido por Shinku)
8. **Merges a `master`:** los hacen solo Shinku o Kurama. Kaguya trabaja en sus ramas y abre los PRs, pero no mezcla a `master` por su cuenta.
9. **Errores de compilación en QA:** si un PR no compila, Shinku lo arregla con el agente de Android Studio y fusiona; avisa en una línea qué era el error. Los errores funcionales van como comentario en el issue y los arregla el agente dueño (el issue vuelve a `en-curso`).

## Plantilla de entrada

### [AAAA-MM-DD] Agente — Título corto

- **Rama:** `agente/tarea`
- **Estado:** en curso | en revisión | fusionado
- **Archivos:** `ruta/Archivo1.kt`, `ruta/Archivo2.kt`
- **Cambios:** descripción breve de lo hecho.
- **Pendiente:** qué falta o qué sigue.

## Registro

### [2026-11-07] Kaguya — Pulido: blancos con alfa del tema (0.7 / 0.6)

- **Rama:** `kaguya/polish-2026-11-07`
- **Estado:** en revisión (PR abierto contra `master`, sin fusionar: regla 8)
- **Archivos:** `app/src/main/java/com/example/example/ui/theme/Color.kt`, `app/src/main/java/com/example/example/ui/MusicPlayerScreen.kt`, `app/src/main/java/com/example/example/ui/NowPlayingScreen.kt`, `app/src/main/java/com/example/example/ui/PlaylistsScreen.kt`, `Agents.changelogs.md`
- **Cambios:** turno de pulido (§8 del flujo de sprints: sin veredicto de Shinku sobre Navigation (issue #9, PR #13) ni sobre Coil (issue #8, con error de compilación KSP pendiente que él está arreglando); ningún issue [Kaguya] en `listo`; WIP en-revision: 1 (#9)). Dos dedups de literales en la UI, sin cambio visual: `Color.White.copy(alpha = 0.7f)` (×16 en las 3 pantallas) pasa a `UchihaWhiteSecondary` y `Color.White.copy(alpha = 0.6f)` (×12) a `UchihaWhiteMuted`, ambos definidos en `theme/Color.kt`. No se toca el catálogo de versiones ni las ramas `kaguya/coil-album-art` / `kaguya/navigation` (su KSP `2.2.10-1.0.31` no resuelve; master ya usa `2.2.10-2.0.2`).
- **Pendiente:** Shinku compila en Android Studio y confirma que la UI se ve igual. Al fusionar, el PR #17 toca las mismas expresiones de `NowPlayingScreen.kt` (`tint = if (isShuffleEnabled) UchihaRed else …` y `tint = if (repeatMode …) UchihaRed else …`): resolver aplicando ambas sustituciones.

### [2026-11-06] Kurama — Regla QA: errores de compilación los arregla Shinku

- **Rama:** `kurama/qa-compile-fix`
- **Estado:** en revisión (PR abierto)
- **Archivos:** `Agents.coordination.md`, `Agents.changelogs.md`
- **Cambios:** regla acordada con Shinku el 2026-11-06 tras el QA de Hilt (PR #6 no compilaba: `ExampleApp` invocaba `hiltViewModel()` en el Preview de Compose y el preview no puede instanciar el ViewModel sin constructor sin argumentos; lo arregló él con el agente de Android Studio y fusionó): si un PR no compila, Shinku lo arregla y fusiona, y avisa en una línea qué era el error; los errores funcionales van como comentario en el issue y los arregla el agente dueño. Aplica igual a los PRs de Kaguya.
- **Pendiente:** Shinku revisa y fusiona; Kaguya lee la regla nueva.

### [2026-11-04] Kurama — Flujo de sprints (coordinación)

- **Rama:** `kurama/sprint-coordination`
- **Estado:** en revisión (PR abierto)
- **Archivos:** `Agents.coordination.md` (nuevo), `Agents.changelogs.md`
- **Cambios:** adoptado el flujo de sprints de 1–2 días: tablero "Uchiha Player — Sprints" (Team Planning) con límite WIP 3 en revisión, convención de issues `[Agente]` + estados `backlog → listo → en-curso → en-revision → hecho`, turnos Kurama 10:00 / Kaguya 14:00 / Shinku QA de noche, reglas de WIP y de noche sin veredicto. Issues del Sprint 1 creados (#7–#11). Kaguya debe leer `Agents.coordination.md` y programar su cron de las 14:00.
- **Pendiente:** Shinku revisa y fusiona; crea el tablero en GitHub con la plantilla Team Planning.

### [2026-11-04] Kurama — Migración a Hilt (inyección de dependencias)

- **Rama:** `kurama/hilt-migration`
- **Estado:** en revisión (PR abierto; pendiente build en Android Studio, sin SDK en la VM)
- **Archivos:** `gradle/libs.versions.toml`, `app/build.gradle.kts`, `UchihaApplication.kt` (nuevo, `@HiltAndroidApp`), `AndroidManifest.xml`, `di/AppModule.kt` (nuevo: provee `UchihaDatabase` singleton + `PlaylistDao`), `data/MediaRepository.kt`, `data/PlaylistRepository.kt`, `data/UserPreferences.kt`, `data/db/UchihaDatabase.kt`, `ui/MusicViewModel.kt`, `ui/ExampleApp.kt`, `MainActivity.kt`, `README.md`
- **Cambios:** Hilt 2.57.1 + KSP + `hilt-navigation-compose` 1.2.0 en el version catalog. `@Inject constructor` en los 3 repositorios/preferencias; Hilt es dueño del singleton de Room (se eliminó el `companion get()` manual de `UchihaDatabase`). `MusicViewModel` es `@HiltViewModel` con `UserPreferences` y `@ApplicationContext` inyectados — se eliminaron los `var` manuales y `initController()` ya no recibe Context. `MainActivity` con `@AndroidEntryPoint`; `ExampleApp` usa `hiltViewModel()` en lugar de la Factory manual y los `remember` de repositorios.
- **Pendiente:** Shinku compila en Android Studio y reporta; al fusionar, avisar a Kaguya porque la migración es transversal (toca `MusicViewModel.kt`/`ExampleApp.kt` de su área UI).

### [2026-10-20] Kurama — Regla de merges y gestión del repo

- **Rama:** `master` (directo, solo docs)
- **Estado:** fusionado
- **Archivos:** `Agents.changelogs.md`
- **Cambios:** agregada la regla 8: los merges a `master` los hacen solo Shinku o Kurama; Kurama gestiona el repositorio.
- **Pendiente:** nada.

### [2026-10-20] Kurama — Setup de coordinación multi-agente

- **Rama:** `kurama/room-playlists`
- **Estado:** fusionado
- **Archivos:** `Agents.changelogs.md` (nuevo)
- **Cambios:** creado este archivo con la división de tareas Kurama / Kaguya y las reglas de coordinación.
- **Pendiente:** Shinku revisa en `master` y crea la rama `Shinku-review` con sus correcciones.

### [2026-10-20] Kaguya — Migración de carátulas a Coil

- **Rama:** `kaguya/coil-album-art`
- **Estado:** en revisión
- **Archivos:** `Agents.changelogs.md`, `gradle/libs.versions.toml`, `app/build.gradle.kts`, `app/src/main/java/com/example/example/ui/components/AlbumArt.kt`
- **Cambios:** registro de tarea antes de tocar código. Alcance: sustituir la carga manual de miniaturas (`ContentResolver.loadThumbnail` + estado) por Coil 3 (`AsyncImage`) manteniendo la misma firma pública de `AlbumArt`, la caché y el fallback Sharingan. Sin cambios en las pantallas que lo usan ni en la capa de datos. Hecho: Coil 3.3.0 (`io.coil-kt.coil3:coil-compose`) en el catálogo, dependencia añadida, `AlbumArt` reescrito con `AsyncImage` + `ImageRequest` (crossfade, placeholder/error = fallback, sin petición si `albumId <= 0`). Sin miniatura fija de 256px: Coil ajusta al tamaño del composable. Llamadas existentes (`MusicPlayerScreen`, `NowPlayingScreen`) sin cambios.
- **Pendiente:** Shinku compila y corre los tests; PR e integración vía Kurama.

### Historial previo (antes de existir este archivo)

- **[2026-10-16] Kurama — Uchiha Player v1.** Fix de cola (`setMediaItems`, auto-avance), seekbar arrastrable, anterior/siguiente, shuffle/repeat, carátulas desde MediaStore (fallback Sharingan), búsqueda, Now Playing completa, DataStore (última canción + posición), notificación personalizada, slider de volumen, temática Uchiha. Fusionado en `master` vía PR #4.
- **[2026-10-19] Kurama — Playlists con Room.** Entidades, DAO, repositorio, pestañas Canciones/Playlists, crear/borrar playlists, detalle con reproducir-todo. Rama `kurama/room-playlists`, pendiente de revisión y merge de Shinku.

## Archivo

*Las entradas ya fusionadas y viejas se resumen aquí para mantener corto el registro activo.*