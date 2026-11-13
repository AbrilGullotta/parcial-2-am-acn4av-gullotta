# CrewUp

App Android para encontrar planes con gente cerca de tu ubicación (picnics, roadtrips, surf, hiking, vóley) y pedir unirte.

Parcial 2 - Aplicaciones Móviles (ACN4AV) - Prof. Sergio Medina

Integrantes: Abril Gullotta

## Usuario de prueba

- Email: pablo@prueba.com
- Contraseña: prueba123

## Pantallas

- Login: ingreso con Firebase Auth. Si ya hay sesión, entra directo.
- Registro: crea la cuenta con nombre, email y contraseña, con validaciones.
- Discover: lista de planes con filtros por categoría. Desde cada tarjeta podés enviar o cancelar la solicitud, y tocándola abrís el detalle. Incluye cerrar sesión.
- Plan Details: info completa del plan (se recibe por Bundle) y botón de solicitud.

## Tecnologías

ConstraintLayout, LinearLayout, ScrollView, Button, TextView, pasaje de datos con Intent + Bundle, strings/dimens/colors en recursos y Firebase Authentication.

## Cómo correrla

Abrir el proyecto en Android Studio, sincronizar Gradle y ejecutar en Android 7.0 (API 24) o superior.
