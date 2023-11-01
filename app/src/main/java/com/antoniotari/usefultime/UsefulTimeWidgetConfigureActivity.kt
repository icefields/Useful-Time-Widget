package com.antoniotari.usefultime

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.antoniotari.usefultime.databinding.UsefulTimeWidgetConfigureBinding

/**
 * The configuration screen for the [UsefulTimeWidget] AppWidget.
 */
class UsefulTimeWidgetConfigureActivity : Activity() {
    private var widgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var binding: UsefulTimeWidgetConfigureBinding

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)
        binding = UsefulTimeWidgetConfigureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWidgetIdFromIntent()

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        bindUI(widgetId)
    }

    private fun bindUI(widgetId: Int) {
        // if widgetId == mainActivityWidgetIdKey it means that we're setting up the
        // time in the main activity and not on the widget
        if (isIdMainActivity(widgetId)) {
            binding.addButton.text = getString(R.string.configure_main)
        }
        binding.addButton.setOnClickListener(this::onSaveClick)
        binding.startTimeText.setText(loadStartTimePref(this@UsefulTimeWidgetConfigureActivity, widgetId).toString())
        binding.endTimeText.setText(loadEndTimePref(this@UsefulTimeWidgetConfigureActivity, widgetId).toString())
    }

    private fun getWidgetIdFromIntent() {
        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        }
    }

    private fun onSaveClick(view: View) {
        // When the button is clicked, store the string locally
        val startTimeWidgetText = binding.startTimeText.text.toString()
        saveStartTimePref(this, widgetId, Integer.parseInt(startTimeWidgetText))
        val endTimeWidgetText = binding.endTimeText.text.toString()
        saveEndTimePref(this, widgetId, Integer.parseInt(endTimeWidgetText))

        // update the widget if the widget id doesn't belong to the activity, otherwise saving
        // the new values is enough for the activity to pick them up once resumed
        if (!isIdMainActivity(widgetId)) {
            // It is the responsibility of the configuration activity to update the app widget
            val appWidgetManager = AppWidgetManager.getInstance(this)
            updateAppWidget(this, appWidgetManager, widgetId)

            // Make sure we pass back the original appWidgetId
            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            setResult(RESULT_OK, resultValue)
        }
        finish()
    }
}
