package com.friendlyrobot.data

import android.content.Context
import com.dropbox.android.external.store4.StoreBuilder

fun provideStore(context: Context) =  StoreBuilder
    .fromNonFlow<Unit, List<CalendarEvent>> {
        ReadCalendar.readCalendar(context)
    }
    .build()
