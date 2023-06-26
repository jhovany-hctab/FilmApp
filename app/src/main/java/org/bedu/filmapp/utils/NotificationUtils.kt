package org.bedu.filmapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.bedu.filmapp.R
import org.bedu.filmapp.ui.profile_users.ProfileUsersFragment
import java.io.IOException
import java.net.URL


suspend fun downloadIconAsync(iconUrl: String): Bitmap? = withContext(Dispatchers.IO) {
    var iconBitmap: Bitmap? = null
    try {
        val inputStream = URL(iconUrl).openStream()
        iconBitmap = BitmapFactory.decodeStream(inputStream)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    iconBitmap
}

@SuppressLint("MissingPermission")
fun simpleNotification(context: Context, nameUser: CharSequence, urlProfile: CharSequence){
    runBlocking {
        val iconBitmap = async { downloadIconAsync(urlProfile as String) }.await()
        if (iconBitmap != null) {
            with(context) {
                val notification = NotificationCompat.Builder(this, ProfileUsersFragment.CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_check_24)
                    .setContentTitle("Ahora sigues a $nameUser")
                    .setContentText(getString(R.string.description))
                    .setLargeIcon(iconBitmap) // ícono grande a la derecha
                    .setStyle(NotificationCompat.BigTextStyle())
                    .build()

                with(NotificationManagerCompat.from(this)) {
                    notify(50, notification)
                }
            }
        } else {
            with(context) {
                val builder = NotificationCompat.Builder(this, ProfileUsersFragment.CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_check_24)
                    .setColor(getColor(R.color.gray_800))
                    .setContentTitle("Ahora sigues a $nameUser")
                    .setContentText(getString(R.string.description))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                //lanzamos la notificación
                NotificationManagerCompat
                    .from(this)
                    .notify(20, builder.build()) // en este caso pusimos un id genérico
            }
        }
    }

}