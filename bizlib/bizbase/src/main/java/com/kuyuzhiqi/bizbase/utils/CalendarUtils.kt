package com.kuyuzhiqi.bizbase.utils

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

//获得本周一0点时间
fun getTimesWeekStart(): Long {
    val cal = Calendar.getInstance()
    cal.firstDayOfWeek = Calendar.MONDAY
    if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        cal.add(Calendar.DAY_OF_MONTH, -1)
    }
    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
    cal.set(Calendar.MILLISECOND, 0)
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    return cal.timeInMillis
}

//获得本周日24点时间
fun getTimesWeekEnd(): Long {
    return getTimesWeekStart() + 7 * 24 * 60 * 60 * 1000L
}

//获得下周周日24点时间
fun getTimesNextWeekEnd(): Long {
    return getTimesWeekStart() + 14 * 24 * 60 * 60 * 1000L
}

//获得本月第一天0点时间
fun getTimesMonthStart(): Long {
    val cal = Calendar.getInstance()
    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
    cal.set(Calendar.MILLISECOND, 0)
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
    return cal.timeInMillis
}

//获得本月最后一天24点时间
fun getTimesMonthEnd(): Long {
    val cal = Calendar.getInstance()
    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
    cal.set(Calendar.MILLISECOND, 0)
    cal.set(Calendar.HOUR_OF_DAY, 24)
    return cal.timeInMillis
}

/**
 * 获取某一天是星期几,1是周日,7是周六
 */
fun getWorkDay(year: Int, month: Int, day: Int): Int {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1)
    calendar.set(Calendar.DATE, day)
    return calendar.get(Calendar.DAY_OF_WEEK)
}

/**
 * 获取当前年份
 * @return
 */
fun getCurrentYear(): String {
    val format = SimpleDateFormat("yyyy")
    return format.format(Date())
}


data class TaskDate(val timestamp: Long, val workDay: Int, val year: Int, val month: Int, val day: Int) :
    Serializable  //workDay 1-周日，7-周六

/**
 * 将时间戳转换为日期形式
 */
fun Long.toTaskDate(): TaskDate {
    val year = SimpleDateFormat("yyyy").format(this).toInt()
    val month = SimpleDateFormat("MM").format(this).toInt()
    val day = SimpleDateFormat("dd").format(this).toInt()
    val workDay = getWorkDay(year, month, day)
    return TaskDate(this, workDay, year, month, day)
}

fun getFormatTaskDate(taskDate: TaskDate): String {
    return taskDate.year.toString() + "." + taskDate.month.toString() + "." + taskDate.day.toString()
}
