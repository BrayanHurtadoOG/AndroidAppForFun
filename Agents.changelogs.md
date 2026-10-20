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

## Plantilla de entrada

### [AAAA-MM-DD] Agente — Título corto

- **Rama:** `agente/tarea`
- **Estado:** en curso | en revisión | fusionado
- **Archivos:** `ruta/Archivo1.kt`, `ruta/Archivo2.kt`
- **Cambios:** descripción breve de lo hecho.
- **Pendiente:** qué falta o qué sigue.

## Registro

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