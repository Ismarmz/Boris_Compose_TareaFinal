# Proyecto Final - App en Jetpack Compose

## Descripción del Proyecto
Esta aplicación móvil desarrollada con **Jetpack Compose** permite el registro de usuarios, gestión de accesos, y consumo de datos desde una API externa. Incluye notificaciones dinámicas para mejorar la experiencia del usuario.

## Funcionalidades Implementadas
- **Registro de Usuarios:** Guardado de datos en una base de datos local usando Room.
- **Inicio de Sesión:** Validación de usuarios registrados y actualización del contador de accesos.
- **Consulta de API:** Consumo de datos de `https://jsonplaceholder.typicode.com/users` usando Retrofit.
- **Notificaciones:** Recordatorios automáticos para consultar la API.
- **Navegación:** Fluida entre pantallas con `NavController`.

## Estructura del Proyecto
```
├── data
│   ├── User.kt
│   ├── UserDao.kt
│   └── AppDatabase.kt
├── ui
│   ├── HomeScreen.kt
│   ├── LoginScreen.kt
│   ├── RegisterScreen.kt
│   └── ApiScreen.kt
└── MainActivity.kt
```

## Cómo Ejecutar la Aplicación
1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-repo/app-compose.git
   ```
2. Abre el proyecto en Android Studio.
3. Ejecuta en un emulador o dispositivo físico con Android 5.0 o superior.

## Permisos Requeridos
- Acceso a Internet
- Permisos de notificación para dispositivos Android 13+

## Requisitos Cumplidos
- [x] Registro y gestión de usuarios
- [x] Persistencia de datos con Room
- [x] Consumo de API externa
- [x] Notificaciones automáticas
- [x] Navegación entre pantallas

## Tecnologías Utilizadas
- **Kotlin** con Jetpack Compose
- **Room Database** para persistencia de datos
- **Retrofit** para consumo de APIs
- **Material 3** para la interfaz de usuario

---

