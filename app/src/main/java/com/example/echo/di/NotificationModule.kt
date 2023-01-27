package com.example.echo.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.echo.R
import com.example.echo.path.MapFragment2
import com.example.echo.service.Constants.ACTION_NAVIGATE_TO_MAPS_FRAGMENT
import com.example.echo.service.Constants.NOTIFICATION_CHANNEL_ID
import com.example.echo.service.Constants.PENDING_INTENT_REQUEST_CODE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {

    @ServiceScoped
    @Provides
    fun providePendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent{
        return PendingIntent.getActivity(
            context,
            PENDING_INTENT_REQUEST_CODE,
            Intent(context, MapFragment2::class.java).apply{
                this.action = ACTION_NAVIGATE_TO_MAPS_FRAGMENT
            },
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_run)
            .setContentIntent(pendingIntent)
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManaver(
        @ApplicationContext context: Context
    ): NotificationManager{
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }




}