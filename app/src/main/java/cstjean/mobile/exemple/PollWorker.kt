package cstjean.mobile.exemple

import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.lang.reflect.Array.get

class PollWorker (
    private val context: Context, workerParameters: WorkerParameters) :
        CoroutineWorker(context, workerParameters) {
        override suspend fun doWork(): Result {
            Log.i("GIPHY", "Work request")

            val preferencesRepository = PreferencesRepository.get()
            val gifRepository = GifRepository()
            val query = preferencesRepository.storedQuery.first()
            if (query.isEmpty()) {
                Log.i("GIPHY", "Pas de query ...")
                return Result.success()
            }

            return try {
                val items = gifRepository.search(query)
                if (items.isNotEmpty()) {
                    // TODO Tester si on a de nouveaux résultats
                    Log.i("GIPHY", "Nouveaux résultats")
                    notifier()
                }
                Result.success()
            } catch (ex: Exception) {
                Log.e("GIPHY", "Work request fail")
                Result.failure()
            }
        }

    private fun notifier() {
        val intent = MainActivity.newIntent(context)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val resources = context.resources

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setTicker(resources.getString(R.string.new_gifs_title))
            .setSmallIcon(android.R.drawable.ic_menu_report_image)
            .setContentTitle(resources.getString(R.string.new_gifs_title))
            .setContentText(resources.getString(R.string.new_gifs_text))
            .setContentIntent(pendingIntent)
            .build()
        NotificationManagerCompat.from(context).notify(0, notification)
    }
}