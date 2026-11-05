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

## Plantilla de entrada

### [AAAA-MM-DD] Agente — Título corto

- **Rama:** `agente/tarea`
- **Estado:** en curso | en revisión | fusionado
- **Archivos:** `ruta/Archivo1.kt`, `ruta/Archivo2.kt`
- **Cambios:** descripción breve de lo hecho.
- **Pendiente:** qué falta o qué sigue.

## Registro

### [2026-11-05] Kaguya — Pulido: eliminar SharinganRed duplicado (turno sin veredicto)

- **Rama:** `kaguya/polish-2026-11-05`
- **Estado:** en revisión (PR abierto contra `master`, sin fusionar: regla 8)
- **Archivos:** `app/src/main/java/com/example/example/ui/theme/Uchiha.kt` (eliminado), `app/src/main/java/com/example/example/ui/theme/Color.kt`, `Agents.changelogs.md`
- **Cambios:** turno de pulido (§8 del flujo de sprints: sin veredicto de Shinku sobre el QA de Hilt (issue #7, PR #6) ni sobre Coil (issue #8), no se toman tareas nuevas). `SharinganRed` estaba declarado dos veces en el mismo paquete `ui.theme` — en `Color.kt` y en `Uchiha.kt` — con el mismo valor (`0xFFE53935`); en Kotlin dos declaraciones top-level con el mismo nombre en el mismo paquete son declaraciones en conflicto, y `Uchiha.kt` solo contenía esa declaración, así que se elimina el archivo y su comentario pasa a `Color.kt`. Ningún uso cambia: todos los archivos importan `SharinganRed` del paquete y el valor es idéntico.
- **Pendiente:** Shinku sincroniza y compila en Android Studio para confirmar que el error de declaración en conflicto desaparece (sin JDK/SDK en la VM de Kaguya, no se pudo compilar aquí). Nota: las ramas abiertas PR #6 (Hilt), PR #13 (Navigation) y `kaguya/coil-album-art` parten de `master` con el duplicado, así que al fusionar esto deberán rebasar.

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

### Historial previo (antes de existir este archivo)

- **[2026-10-16] Kurama — Uchiha Player v1.** Fix de cola (`setMediaItems`, auto-avance), seekbar arrastrable, anterior/siguiente, shuffle/repeat, carátulas desde MediaStore (fallback Sharingan), búsqueda, Now Playing completa, DataStore (última canción + posición), notificación personalizada, slider de volumen, temática Uchiha. Fusionado en `master` vía PR #4.
- **[2026-10-19] Kurama — Playlists con Room.** Entidades, DAO, repositorio, pestañas Canciones/Playlists, crear/borrar playlists, detalle con reproducir-todo. Rama `kurama/room-playlists`, pendiente de revisión y merge de Shinku.

## Archivo

*Las entradas ya fusionadas y viejas se resumen aquí para mantener corto el registro activo.*