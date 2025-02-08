package com.example.boris_compose_tareafinal

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

fun mostrarNotificacion(context: Context, mensaje: String) {
    val channelId = "mi_canal_id"
    val notificationId = System.currentTimeMillis().toInt() // ID único para cada notificación

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground) // Asegúrate de que el ícono existe
        .setContentTitle("Recordatorio")
        .setContentText(mensaje)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    // Verificar permisos antes de mostrar la notificación (Android 13+)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
        ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    ) {
        NotificationManagerCompat.from(context).notify(notificationId, builder.build())
    }
}
