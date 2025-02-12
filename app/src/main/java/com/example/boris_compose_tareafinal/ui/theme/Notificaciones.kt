package com.example.boris_compose_tareafinal

import android.content.Context
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

fun mostrarNotificacion(context: Context, mensaje: String) {
    //channel id must be the same when we create the channel in the main activity
    val channelId = "mi_canal_id"
    //unique id for each notification, set the current time
    val notificationId = System.currentTimeMillis().toInt()

    //build a notify
    val builder = NotificationCompat.Builder(context, channelId)
        //set tittle
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        //tittle
        .setContentTitle("Recordatorio")
        //message
        .setContentText(mensaje)
        //level of priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        //destroy the notify when the user touch it
        .setAutoCancel(true)

    //check the permissions and show the notify
    //we need to ask permission if the users have android 13+
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
        ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
    ) {
        NotificationManagerCompat.from(context).notify(notificationId, builder.build())
    }
}
