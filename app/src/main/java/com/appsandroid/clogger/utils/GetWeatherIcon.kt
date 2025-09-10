package com.appsandroid.clogger.utils

import com.appsandroid.clogger.R

fun getWeatherIcon(code: Int): Int {
    return when (code) {
        0 -> R.drawable.baseline_sunny_24
        in 1..3 -> R.drawable.baseline_explore_24
        in 45..48 -> R.drawable.baseline_brightness_2_24
        in 51..67 -> R.drawable.baseline_thermostat_24
        in 71..77 -> R.drawable.baseline_air_24
        in 95..99 -> R.drawable.baseline_cloudy_snowing_24
        else -> R.drawable.baseline_analytics_24
    }
}