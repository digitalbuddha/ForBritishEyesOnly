package com.friendlyrobot.data

import android.content.Context
import com.dropbox.android.external.store4.StoreBuilder
typealias CalendarId=String

fun provideEventStore(context: Context) =  StoreBuilder
    .fromNonFlow<CalendarId, List<CalendarEvent>> {
        ReadCalendar.getEventsFor(it,context)
    }
    .build()

fun provideCalendarStore(context: Context) =  StoreBuilder
    .fromNonFlow<Unit, Set<CalendarName>> {
        ReadCalendar.readCalendar(context)
    }
    .build()
