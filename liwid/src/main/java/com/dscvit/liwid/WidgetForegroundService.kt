package com.dscvit.liwid
import android.Manifest
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dscvit.liwid.api.model.SportsData
import com.dscvit.liwid.api.model.TrackerData
import com.dscvit.liwid.widget.LiveSportsWidget
import com.dscvit.liwid.widget.LiveTrackingWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WidgetForegroundService:Service() {
    companion object{
        const val NOTIFICATION_KEY: String = "notification"
        const val WIDGET_TYPE_KEY: String = "widgetType"

        fun startService(
            context: Context,
            widgetType: LiveWidget.WidgetType
        ) {
            val serviceIntent = Intent(context, WidgetForegroundService::class.java).apply {
                putExtra(WIDGET_TYPE_KEY, widgetType)
            }
            ContextCompat.startForegroundService(context, serviceIntent)
            Log.d("WidgetForegroundService", "Service started")
        }

        fun stopService(context:Context){
            val serviceIntent = Intent(context, WidgetForegroundService::class.java)
            context.stopService(serviceIntent)
            Log.d("WidgetForegroundService", "Service stopped")
        }
    }

    private val channelID = "ForegroundServiceChannel"
    private var notificationJob: Job? = null
    private val notificationID:Int=1


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification=intent?.getParcelableExtra<Notification>(NOTIFICATION_KEY)
        val widgetType=intent?.getSerializableExtra(WIDGET_TYPE_KEY) as LiveWidget.WidgetType
        if(notification!=null) {
            startForeground(notificationID, notification)
            notificationJob = GlobalScope.launch(Dispatchers.IO) {
                while(isActive){
                    fetchDataAndUpdateWidget(widgetType)
                    if(widgetType== LiveWidget.WidgetType.SPORTS){
                        delay(60000)
                    }else{
                        delay(60*5*1000)
                    }
                }
            }
        }
        Log.d("WidgetForegroundService", "onStartCommand started")
        return START_STICKY
    }
    private var resSportsData: SportsData? = null
    private var resTrackingData: TrackerData? = null
    private fun fetchDataAndUpdateWidget(widgetType: LiveWidget.WidgetType) {
        try {
           if (widgetType == LiveWidget.WidgetType.SPORTS) {
                var resSportsData: Unit = LiveSportsWidget.fetchSportsData()
            } else {
               var resTrackingData: Unit = LiveTrackingWidget.fetchTrackingData()
            }
            println(resSportsData)
            Log.d("WidgetForegroundService", "Data fetched")
            // Update the notification content based on the API response data
            val notificationContent = when (widgetType) {
                LiveWidget.WidgetType.SPORTS -> resSportsData
                LiveWidget.WidgetType.TRACKING -> resTrackingData
            }

            // Create the updated notification
            val updatedNotification = when (widgetType) {
                LiveWidget.WidgetType.SPORTS -> createSportsWidget(notificationContent as SportsData)
                LiveWidget.WidgetType.TRACKING -> createTrackingWidget(notificationContent as TrackerData)
            }

            // Notify the notification manager
            with(NotificationManagerCompat.from(this)) {
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notify(notificationID, updatedNotification)
                }
            }
        } catch (e: Exception) {
            Log.d("WidgetForegroundService", "Error fetching data: ${e.message}")
        }
    }

//    val sportsNotificationLayoutExp= RemoteViews("com.example.liwid_app",R.layout.sports_widget_layout_expanded)
    private val trackingNotificationLayout= RemoteViews(baseContext.packageName!!,R.layout.tracking_widget_layout_wrapped)
    private val sportsNotificationLayout= RemoteViews(baseContext.packageName!!,R.layout.sports_widget_layout_wrapped)

    private fun createSportsWidget(notificationContent: SportsData): Notification {
        sportsNotificationLayout.setTextViewText(R.id.league_name,notificationContent.leagueName)
        sportsNotificationLayout.setTextViewText(R.id.homeTeamName,notificationContent.homeTeamName)
        sportsNotificationLayout.setTextViewText(R.id.awayTeamName,notificationContent.awayTeamName)
        sportsNotificationLayout.setTextViewText(R.id.homeTeamScore,notificationContent.homeTeamResult.toString())
        sportsNotificationLayout.setTextViewText(R.id.awayTeamScore,notificationContent.awayTeamResult.toString())
        sportsNotificationLayout.setTextViewText(R.id.centralMatchResult,notificationContent.matchResult)
        sportsNotificationLayout.setTextViewText(R.id.eventStatus,notificationContent.eventStatus)
        sportsNotificationLayout.setTextViewText(R.id.smallAwayLogo,notificationContent.awayTeamLogo)
        sportsNotificationLayout.setTextViewText(R.id.smallHomeLogo,notificationContent.homeTeamLogo)
        val homeTeamLogoBitmap=Glide.with(this).asBitmap().load(notificationContent.homeTeamLogo).submit().get()
        val awayTeamLogoBitmap=Glide.with(this).asBitmap().load(notificationContent.awayTeamLogo).submit().get()
        sportsNotificationLayout.setImageViewBitmap(R.id.smallHomeLogo,homeTeamLogoBitmap)
        sportsNotificationLayout.setImageViewBitmap(R.id.smallAwayLogo,awayTeamLogoBitmap)
        Log.d("WidgetForegroundService", "Sports widget updated")
        // Implement notification creation logic for SPORTS widget
        return NotificationCompat.Builder(this, channelID)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(sportsNotificationLayout)
//            .setCustomBigContentView(notificationLayoutExpanded)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .build()
    }

    private fun createTrackingWidget(notificationContent: TrackerData): Notification {
        trackingNotificationLayout.setTextViewText(R.id.orderStatus,notificationContent.orderStatus)
        // Implement notification creation logic for TRACKING widget
        return NotificationCompat.Builder(this, channelID)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(trackingNotificationLayout)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .build()
    }


    override fun onDestroy() {
        super.onDestroy()
        notificationJob?.cancel()
        stopForeground(true)
        stopSelf()
        Log.d("WidgetForegroundService", "Service stopped")
    }
}
