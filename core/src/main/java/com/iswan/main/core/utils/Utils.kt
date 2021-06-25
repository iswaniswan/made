package com.iswan.main.core.utils

import android.os.Build
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*

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

        when {
            days < 7 -> days.let {
                dwmy = if (it > 1) "$it days" else "$it day"
            }
            days in 7..30 -> days.let {
                val week = (it.toDouble() / 7).toInt()
                dwmy = if (week > 1) "$week weeks" else "$week week"
            }
            days in 31..365 -> days.let {
                val month = (it.toDouble()/30).toInt()
                dwmy = if (month > 1) "$month months" else "$month month"
            }
            else -> days.let {
                val year = (it.toDouble()/365).toInt()
                dwmy = if (year > 1) "$year years" else "$year year"
            }
        }

        return "about $dwmy ago"
    }
}