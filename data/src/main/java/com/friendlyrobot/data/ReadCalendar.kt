package com.friendlyrobot.data

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.text.format.DateUtils
import android.util.Log
import java.util.Date
import java.util.HashSet
import java.util.regex.Pattern

fun println(message: String) = Log.i("tag", message)

var output = mutableListOf<CalendarEvent>()

data class CalendarEvent(val title: String, val start: Date, val end: Date)
data class CalendarName( val id:String, val title: String)

fun println(calendarEvent: CalendarEvent) {
    output.add(calendarEvent)
}

object ReadCalendar {
    var cursor: Cursor? = null
    fun readCalendar(context: Context): HashSet<CalendarName> {
        return getCalendars(context)
    }

     fun getEventsFor(
        id: CalendarId,
        context: Context): MutableList<CalendarEvent> {
         val contentResolver = context.contentResolver
         // For each calendar, display all the events from the previous week to the end of next week.
            val builder =
                Uri.parse("content://com.android.calendar/instances/when").buildUpon()
            //Uri.Builder builder = Uri.parse("content://com.android.calendar/calendars").buildUpon();
            val now = Date().time
            ContentUris.appendId(builder, now)
            ContentUris.appendId(builder, now + DateUtils.DAY_IN_MILLIS * 10000)
            val eventCursor = contentResolver.query(
                builder.build(),
                arrayOf("title", "begin", "end", "allDay"),
                CalendarContract.Instances.CALENDAR_ID + "=" + id,
                null,
                "startDay ASC, startMinute ASC"
            )
            println("eventCursor count=" + eventCursor!!.count)
            if (eventCursor.count > 0) {
                if (eventCursor.moveToFirst()) {
                    do {
                        var mbeg_date: Any
                        var beg_date: Any
                        var beg_time: Any
                        var end_date: Any
                        var end_time: Any
                        val title = eventCursor.getString(0)
                        val begin = Date(eventCursor.getLong(1))
                        val end = Date(eventCursor.getLong(2))
                        val allDay = eventCursor.getString(3) != "0"
                        /*  System.out.println("Title: " + title + " Begin: " + begin + " End: " + end +
                                    " All Day: " + allDay);
                        */
                        println(CalendarEvent(title, begin, end))

                        // println("All Day:$allDay")
                        /* the calendar control metting-begin events Respose  sub-string (starts....hare) */
                        val p = Pattern.compile(" ")
                        val items = p.split(begin.toString())
                        var scalendar_metting_beginday: String
                        var scalendar_metting_beginmonth: String
                        var scalendar_metting_beginyear: String
                        var scalendar_metting_begindate: String
                        var scalendar_metting_begintime: String
                        var scalendar_metting_begingmt: String
                        scalendar_metting_beginday = items[0]
                        scalendar_metting_beginmonth = items[1]
                        scalendar_metting_begindate = items[2]
                        scalendar_metting_begintime = items[3]
                        scalendar_metting_begingmt = items[4]
                        scalendar_metting_beginyear = items[5]
                        val calendar_metting_beginday = scalendar_metting_beginday
                        val calendar_metting_beginmonth =
                            scalendar_metting_beginmonth.trim { it <= ' ' }
                        val calendar_metting_begindate =
                            scalendar_metting_begindate.trim { it <= ' ' }.toInt()
                        val calendar_metting_begintime =
                            scalendar_metting_begintime.trim { it <= ' ' }
                        val calendar_metting_begingmt = scalendar_metting_begingmt
                        val calendar_metting_beginyear =
                            scalendar_metting_beginyear.trim { it <= ' ' }.toInt()
                        // println("calendar_metting_beginday=$calendar_metting_beginday")
                        // println("calendar_metting_beginmonth =$calendar_metting_beginmonth")
                        // println("calendar_metting_begindate =$calendar_metting_begindate")
                        // println("calendar_metting_begintime=$calendar_metting_begintime")
                        // println("calendar_metting_begingmt =$calendar_metting_begingmt")
                        // println("calendar_metting_beginyear =$calendar_metting_beginyear")
                        /* the calendar control metting-begin events Respose  sub-string (starts....ends) */ /* the calendar control metting-end events Respose  sub-string (starts....hare) */
                        val p1 = Pattern.compile(" ")
                        val enditems = p.split(end.toString())
                        var scalendar_metting_endday: String
                        var scalendar_metting_endmonth: String
                        var scalendar_metting_endyear: String
                        var scalendar_metting_enddate: String
                        var scalendar_metting_endtime: String
                        var scalendar_metting_endgmt: String
                        scalendar_metting_endday = enditems[0]
                        scalendar_metting_endmonth = enditems[1]
                        scalendar_metting_enddate = enditems[2]
                        scalendar_metting_endtime = enditems[3]
                        scalendar_metting_endgmt = enditems[4]
                        scalendar_metting_endyear = enditems[5]
                        val calendar_metting_endday = scalendar_metting_endday
                        val calendar_metting_endmonth =
                            scalendar_metting_endmonth.trim { it <= ' ' }
                        val calendar_metting_enddate =
                            scalendar_metting_enddate.trim { it <= ' ' }.toInt()
                        val calendar_metting_endtime =
                            scalendar_metting_endtime.trim { it <= ' ' }
                        val calendar_metting_endgmt = scalendar_metting_endgmt
                        val calendar_metting_endyear =
                            scalendar_metting_endyear.trim { it <= ' ' }.toInt()
                        // println("calendar_metting_beginday=$calendar_metting_endday")
                        // println("calendar_metting_beginmonth =$calendar_metting_endmonth")
                        // println("calendar_metting_begindate =$calendar_metting_enddate")
                        // println("calendar_metting_begintime=$calendar_metting_endtime")
                        // println("calendar_metting_begingmt =$calendar_metting_endgmt")
                        // println("calendar_metting_beginyear =$calendar_metting_endyear")
                        /* the calendar control metting-end events Respose  sub-string (starts....ends) */
                        // println(
                        //     "only date begin of events=" + begin.date
                        // )
                        // println("only begin time of events=" + begin.hours + ":" + begin.minutes + ":" + begin.seconds)
                        // println("only date begin of events=" + end.date)
                        // println("only begin time of events=" + end.hours + ":" + end.minutes + ":" + end.seconds)
                        beg_date = begin.date
                        mbeg_date =
                            begin.date.toString() + "/" + calendar_metting_beginmonth + "/" + calendar_metting_beginyear
                        beg_time = begin.hours
                        // println("the vaule of mbeg_date=" + mbeg_date.toString().trim { it <= ' ' })
                        end_date = end.date
                        end_time = end.hours
                        println("\n \n")
                    } while (eventCursor.moveToNext())
                }
            }
        return output
    }

     fun getCalendars(context: Context): HashSet<CalendarName> {
        val contentResolver = context.contentResolver
        // Fetch a list of all calendars synced with the device, their display names and whether the
        cursor = contentResolver.query(
            Uri.parse("content://com.android.calendar/calendars"),
            arrayOf(
                "_id",
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.IS_PRIMARY
            ), null, null, null
        )
        val calendarIds = HashSet<CalendarName>()
        try {
            println("Count=" + cursor!!.count)
            if (cursor!!.count > 0) {
                println("the control is just inside of the cursor.count loop")
                while (cursor!!.moveToNext()) {
                    val _id = cursor!!.getString(0)
                    val displayName = cursor!!.getString(1)
                    val selected = cursor!!.getString(2) != "0"
                    println("Id: $_id Display Name: $displayName Selected: $selected")
                    calendarIds.add(CalendarName(_id, displayName))
                }
            }
        } catch (ex: AssertionError) {
            ex.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return calendarIds
    }
}