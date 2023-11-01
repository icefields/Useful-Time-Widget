package com.antoniotari.usefultime

import android.content.Context

const val PREFS_KEY = "com.antoniotari.usefultime.UsefulTimeWidget"
private const val PREF_PREFIX_KEY = "appwidget_"
const val startTimeKey = "startTimeWidgetId"
const val endTimeKey = "endTimeWidgetId"
const val mainActivityWidgetIdKey = 11

// Write the prefix to the SharedPreferences object for this widget
internal fun saveTimePref(context: Context, appWidgetIdPlusKey: String, text: Int) {
    val prefs = context.getSharedPreferences(PREFS_KEY, 0).edit()
    prefs.putInt(PREF_PREFIX_KEY + appWidgetIdPlusKey, text)
    prefs.apply()
}

internal fun saveStartTimePref(context: Context, appWidgetId: Int, text: Int) {
    saveTimePref(context, startTimeKey + appWidgetId, text)
}

internal fun saveEndTimePref(context: Context, appWidgetId: Int, text: Int) {
    saveTimePref(context, endTimeKey + appWidgetId, text)
}

// Read the prefix from the SharedPreferences object for this widget.
// If there is no preference saved, get the default from a resource
internal fun loadTimePref(context: Context, appWidgetIdPlusKey: String, defaultValue: Int): Int {
    val prefs = context.getSharedPreferences(PREFS_KEY, 0)
    return prefs.getInt(PREF_PREFIX_KEY + appWidgetIdPlusKey, defaultValue)
}

internal fun loadStartTimePref(context: Context, appWidgetId: Int): Int {
    return loadTimePref(context, startTimeKey + appWidgetId, 9)
}

internal fun loadEndTimePref(context: Context, appWidgetId: Int): Int {
    return loadTimePref(context, endTimeKey + appWidgetId, 22)
}

internal fun deleteTimePref(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_KEY, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + startTimeKey + appWidgetId)
    prefs.remove(PREF_PREFIX_KEY + endTimeKey + appWidgetId)
    prefs.apply()
}

internal fun isIdMainActivity(appWidgetId: Int): Boolean {
    return appWidgetId == mainActivityWidgetIdKey
}