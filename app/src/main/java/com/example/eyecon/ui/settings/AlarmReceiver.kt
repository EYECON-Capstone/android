package com.example.eyecon.ui.settings

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.eyecon.R
import java.util.*
import java.text.ParseException
import java.text.SimpleDateFormat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        Log.d("AlarmReceiver", "Type: $type, Message: $message")


        if (type.equals(TYPE_REPEATING, ignoreCase = true) && message != null) {
            showAlarmNotification(context, TYPE_REPEATING, message, ID_REPEATING)
        }
    }

    // Gunakan metode ini untuk menampilkan notifikasi
    private fun showAlarmNotification(
        context: Context,
        title: String,
        message: String,
        notifId: Int
    ) {

        val channelId = "Channel_1"
        val channelName = "AlarmManager channel"

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.eyecon_green)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        /*
        Untuk android Oreo ke atas perlu menambahkan notification channel
        Materi ini akan dibahas lebih lanjut di modul extended
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(channelId)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()

        notificationManagerCompat.notify(notifId, notification)

    }


    // Metode ini digunakan untuk menjalankan alarm repeating
    fun setRepeatingAlarm(context: Context, type: String, time: String, message: String) {

        // Validasi inputan waktu terlebih dahulu
        if (isDateInvalid(time, TIME_FORMAT)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        Log.d("setRepeatingAlarm", "Message: $message")
        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_REPEATING,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(context, "Repeating alarm set up", Toast.LENGTH_SHORT).show()
    }
    fun cancelAlarm(context: Context, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        // Gunakan ID yang sesuai berdasarkan tipe alarm
        val requestCode = if (type.equals(TYPE_REPEATING, ignoreCase = true)) ID_REPEATING else null
        if (requestCode == null) {
            Toast.makeText(context, "Alarm type not recognized", Toast.LENGTH_SHORT).show()
            Log.e("cancelAlarm", "Unknown alarm type: $type")
            return
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE // Gunakan FLAG_NO_CREATE untuk menghindari membuat PendingIntent baru
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Toast.makeText(context, "Alarm canceled successfully", Toast.LENGTH_SHORT).show()
            Log.d("cancelAlarm", "Alarm with type $type and requestCode $requestCode canceled")
        } else {
            Toast.makeText(context, "No active alarm found to cancel", Toast.LENGTH_SHORT).show()
            Log.d("cancelAlarm", "No active alarm found with type $type and requestCode $requestCode")
        }
    }


    // Metode ini digunakan untuk validasi date dan time
    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }

    companion object {
        const val TYPE_REPEATING = "Pengingat Harian"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        // Siapkan 2 id untuk 2 macam alarm, onetime dan repeating
        private const val ID_REPEATING = 101
        private const val TIME_FORMAT = "HH:mm"

    }
}