package com.iswan.main.core.ui

import android.os.Build
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*
import kotlin.math.floor

object Utils {

    fun simplifyOfNumber(counts: Int, unit: String = ""): String {
        val hit: String
        if (counts > 999999) {
            hit = (counts / 1000000).toString() + "M"
        } else if (counts <= 999999 && counts > 999) {
            hit = (counts / 1000).toString() + "K"
        } else {
            hit = counts.toString()
        }
        return hit + unit
    }

    fun extractISOTimeToString(strTime: String): String {
        return strTime
            .replace("PT", "")
            .replace("H", ":")
            .replace("M", ":")
            .replace("S", "")
    }


    fun converISO8601toDate(strDate: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ta: TemporalAccessor = DateTimeFormatter.ISO_DATE_TIME.parse(strDate)
            val ld = LocalDate.from(ta)
            val year = ld.year
            val month = ld.month
            val day = ld.dayOfMonth
            return "$month $day $year"
        } else {
            val sd = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
            val dd = sd.parse(strDate)
            val cal = Calendar.getInstance()
            cal.time = dd
            val year = cal.get(Calendar.YEAR)
            val m1 = cal.get(Calendar.MONTH)
            val dfs = DateFormatSymbols.getInstance()
            val month = dfs.months.get(m1)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            return "$month $day $year"
        }
    }

    fun convertISO8601toTimeAgo(strDate: String): String {
        val dateP = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        val date1 = dateP.parse(strDate)

        val now = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
        val date2 = df.parse(now)

        val diff: Long = date2.time - date1.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return if (days.toInt() == 0) {
            if (hours.toInt() == 0) showAsMin(minutes)
            else showAsH24(hours)
        } else showAsDWMY(days.toInt())
    }

    private fun showAsMin(minutes: Long): String = "about $minutes minutes ago"

    private fun showAsH24(hours: Long): String = "about $hours ago"

    private fun showAsDWMY(days: Int): String {
        var dwmy: String
        if (days < 7) {
            if (days > 1) dwmy = days.toString() + " days"
            else dwmy = days.toString() + " day"
        } else if (days >= 7 && days < 30) {
            val w = floor(days.toDouble() / 7).toInt()
            if (w > 1) dwmy = w.toString() + " weeks"
            else dwmy = w.toString() + " week"
        } else if (days >= 30 && days < 365) {
            val m = floor(days.toDouble() / 30).toInt()
            if (m > 1) dwmy = m.toString() + " months"
            else dwmy = m.toString() + " month"
        } else if (days >= 365) {
            val y = floor(days.toDouble() / 365).toInt()
            if (y > 1) dwmy = y.toString() + " years"
            else dwmy = y.toString() + " year"
        } else dwmy = "long"

        return "about $dwmy ago"
    }

}