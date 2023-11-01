package com.antoniotari.usefultime

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import java.util.*

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [UsefulTimeWidgetConfigureActivity]
 */
class UsefulTimeWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d("aaaaaa", "onUpdate")

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteTimePref(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val startTime = loadStartTimePref(context, appWidgetId)
    val endTime = loadEndTimePref(context, appWidgetId)

    val totalTimeHours = endTime - startTime
    val totalTimeHalfHours = totalTimeHours * 2
    val currentDate = Date()
    val elapsedHours = currentDate.hours - startTime
    val elapsedHalfHours = elapsedHours * 2 +
            if (currentDate.minutes > 30) {1} else {0}

    val views = RemoteViews(context.packageName, R.layout.useful_time_widget)
    views.setProgressBar(R.id.progressBar, totalTimeHalfHours, elapsedHalfHours, false)
    views.setTextViewText(R.id.unitsTextView, context.getString(R.string.start_end_units, elapsedHalfHours, totalTimeHalfHours))
    views.setTextViewText(R.id.timeStartEndTextView, context.getString(R.string.start_end_time, startTime, endTime))
    appWidgetManager.updateAppWidget(appWidgetId, views)
}