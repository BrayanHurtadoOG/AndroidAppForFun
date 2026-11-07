# Coordinación por sprints — Uchiha Player

Flujo de trabajo adoptado el 2026-11-04. Si trabajas en este repo (Kurama, Kaguya),
lee este archivo completo antes de tocar código. El changelog (`Agents.changelogs.md`)
sigue existiendo para registrar cambios; este archivo define **cómo** trabajamos.

## Sprint actual: 1 (5–6 nov 2026)

> Para abrir un sprint nuevo, Shinku (o Kurama a su pedido) actualiza esta línea
> y mueve issues de backlog a listo.

## 1. El tablero

GitHub Project **"Uchiha Player — Sprints"** (plantilla Team Planning), vinculado al repo.
Es el panel visual de Shinku. Columnas (campo Status):

- **Backlog** — ideas y tareas futuras, sin compromiso.
- **Sprint actual** — lo comprometido para el sprint en curso.
- **En progreso** — alguien la está implementando ahora.
- **En revisión** — PR abierto, esperando el veredicto de Shinku. **Límite: 3.** Si está llena, nadie toma tareas nuevas.
- **Hecho** — probado por Shinku en su máquina/teléfono. Solo él mueve tarjetas aquí.

Los agentes trabajan sobre los **issues** (ver sección 4); Shinku mueve las tarjetas
del tablero en su revisión nocturna. Si un issue cambia de estado, su tarjeta debe
reflejarlo: el que mueve el estado avisa y Shinku lo refleja en el tablero.

## 2. Roles

- **Shinku** — product owner y QA. Define los issues con criterios de aceptación,
  prueba en Android Studio / su teléfono, dicta el veredicto (`ok` o qué arreglar),
  mueve tarjetas a Hecho y fusiona a `master`.
- **Kurama** — núcleo, datos e integración. Turno **10:00** (America/Havana).
- **Kaguya** — capa visual / UI. Turno **14:00** (America/Havana).

Regla 8 vigente: los merges a `master` los hacen solo Shinku o Kurama.
Kaguya abre PRs pero no mezcla.

## 3. Horarios (America/Havana)

| Quién   | Hora  | Qué hace |
|---------|-------|----------|
| Kurama  | 10:00 | Ejecuta su turno (sección 5). Deja su PR abierto al mediodía. |
| Kaguya  | 14:00 | Ejecuta su turno (sección 6). Si su tarea depende del PR de Kurama, construye sobre su rama; si no, trabaja en paralelo. |
| Shinku  | noche (17:00–20:00 aprox.) | Revisa PRs, compila, prueba en teléfono, dicta veredictos en cada issue. |

El escalonamiento evita que ambos agentes pusheen a la vez y le da a Kaguya la
opción de apoyarse en el trabajo de la mañana.

## 4. Convención de issues

- **Título:** `[Kurama] …`, `[Kaguya] …`, `[QA] …` o `[Shinku] …` según quién la ejecuta.
- **Primera línea del cuerpo:** `Sprint: N · Estado: <estado> · Agente: <nombre>`
- **Estados:** `backlog` → `listo` → `en-curso` → `en-revision` → `hecho` (issue cerrado).
  - `listo`: entra al sprint (lo decide Shinku).
  - `en-curso`: el agente la tomó (actualiza el cuerpo del issue).
  - `en-revision`: PR abierto (el agente comenta el link del PR en el issue).
  - `hecho`: Shinku probó y dio `ok` → se cierra el issue.
  - Si el veredicto pide arreglos: vuelve a `en-curso` con el comentario de Shinku describiendo el fallo (salvo errores de compilación: ver sección 7).
- Todo issue de implementación trae **Contrato** (rama, archivos que toca, qué NO toca)
  y **Criterios de aceptación** (cómo Shinku lo va a probar). Sin eso, el issue no está
  listo y el agente no la toma: un issue ambiguo es un día perdido.

## 5. Turno de Kurama (10:00)

1. Lee este archivo (por si el sprint o las reglas cambiaron).
2. Busca issues abiertos con `[Kurama]` en el título. Lee el cuerpo de cada uno.
3. **Regla WIP:** cuenta tus issues en `en-revision`. Si hay 3 o más, NO tomes tarea
   nueva: dedica el turno a pulir (tests, docs, refactor menor) en rama
   `kurama/polish-<fecha>`, con PR igualmente.
4. Si Shinku dejó un veredicto con arreglos pedidos, atiéndelo primero
   (el issue vuelve a `en-curso`).
5. Toma el issue `listo` más antiguo: actualiza su estado a `en-curso`.
6. Sincroniza `master` local, crea `kurama/<tarea>`, implementa.
7. Registra la entrada en `Agents.changelogs.md` (plantilla de ese archivo).
8. Push por el flujo sancionado (GitHub MCP snapshot-push) y abre PR contra `master`.
9. Marca el issue `en-revision` y comenta el link del PR.
10. **Nunca** fusiones a `master` sin que Shinku lo pida. **Nunca** toques archivos del
    área de Kaguya sin registrarlo antes en el changelog.
11. Si no hay issues `listo`, informa el estado y termina: no inventes tareas.

## 6. Turno de Kaguya (14:00)

Lo mismo que la sección 5, adaptado:

1. Lee este archivo.
2. Busca issues abiertos con `[Kaguya]` en el título.
3. **Regla WIP:** igual, límite de 3 en `en-revision`.
4. Atiende primero veredictos con arreglos pedidos.
5. Toma el issue `listo` más antiguo → estado `en-curso`.
6. Sincroniza `master` (o la rama de Kurama si tu issue depende de su PR de la mañana),
   crea `kaguya/<tarea>`, implementa.
7. Registra en `Agents.changelogs.md`, push por tu flujo, abre PR contra `master`.
8. Marca el issue `en-revision` y comenta el link del PR.
9. **No fusiones a `master`** (regla 8). No toques `data/`, `di/` ni núcleo sin registrarlo.
10. Si no hay issues `listo`, informa y termina.

Para programar este turno, crea en tu chat un cron diario a las 14:00
(America/Havana) con estas instrucciones como cuerpo.

## 7. Turno de Shinku (noche)

1. Revisa los PRs del día: compila en Android Studio, prueba en el teléfono.
2. En cada issue en `en-revision`, comenta el veredicto:
   - `ok` → el agente (o tú) cierra el issue; mueves la tarjeta a Hecho.
   - Fallos funcionales → describes qué falla (log si puedes); el issue vuelve a `en-curso`.
   - **Errores que impiden compilar** → los arreglas tú con el agente de Android Studio y fusionas, sin esperar al agente dueño: sin compilar no se puede probar lo funcional. Le dices al agente dueño en una línea qué era el error (import, manifest, versión del catálogo, etc.) para no repetirlo. Los agentes no pueden compilar en la VM (sin Android SDK), así que sus PRs llegan sin prueba de compilación real.
3. Mueve las tarjetas del tablero según el estado real.
4. Fusiona a `master` lo que esté en `ok` (o pídele a Kurama que lo haga).

## 8. Si no hay veredicto (noche de gym, día ocupado)

Si Shinku no pudo revisar, al día siguiente los agentes **no toman tareas nuevas**:
dedican el turno a pulir lo pendiente (tests, documentación, refactors pequeños,
revisar la rama `shinku-review`) o a adelantar issues en `backlog` solo si Shinku
lo pidió. La idea es no acumular trabajo sin verificar.

## 9. Cierre y apertura de sprint

Sprint de 1–2 días. Al cerrarlo:
1. Todo issue del sprint está `hecho` (cerrado) o movido al siguiente sprint.
2. Shinku (o Kurama a su pedido) actualiza `Sprint actual` arriba y mueve issues
   de Backlog a `listo` para el sprint nuevo.
3. Breve retro en el issue `[Shinku] Sprint N: dirigir el sprint`: qué salió bien,
   qué ajustar.

## 10. Ramas y changelog (resumen)

Ramas `kurama/<tarea>` / `kaguya/<tarea>`; todo a `master` por PR; entradas en
`Agents.changelogs.md` con la plantilla de ese archivo antes y después de cada tarea.
Detalle completo en `Agents.changelogs.md` (reglas 1–8).

## 11. Nota de escalado

Este flujo se está probando en Uchiha Player. Si funciona, Shinku lo aplicará a más
de un proyecto a la vez, con su ventana nocturna de revisión como límite real del
sistema (~6 h, tope de batería). Los agentes no generan más de lo que Shinku puede
revisar: el WIP de la sección 1 existe para eso.
