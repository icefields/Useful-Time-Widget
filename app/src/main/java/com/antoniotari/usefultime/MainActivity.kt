package com.antoniotari.usefultime

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.antoniotari.usefultime.databinding.ActivityMainBinding
import com.antoniotari.usefultime.databinding.UsefulTimeWidgetConfigureBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        binding.configBtn.setOnClickListener {
            val intent = Intent(this, UsefulTimeWidgetConfigureActivity::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mainActivityWidgetIdKey)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val startTime = loadStartTimePref(this, mainActivityWidgetIdKey)
        val endTime = loadEndTimePref(this, mainActivityWidgetIdKey)
        val totalTimeHours = endTime - startTime
        val totalTimeHalfHours = totalTimeHours * 2
        val currentDate = Date()
        val elapsedHours = currentDate.hours - startTime
        val elapsedHalfHours = elapsedHours * 2 +
                if (currentDate.minutes > 30) {1} else {0}

        binding.progressBarMain.max = totalTimeHalfHours
        binding.progressBarMain.progress = elapsedHalfHours
        binding.unitsMainTextView.text = getString(R.string.start_end_units, elapsedHalfHours, totalTimeHalfHours)
        binding.timeStartEndMainTextView.text = getString(R.string.start_end_time, startTime, endTime)
    }
}