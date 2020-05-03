package com.friendlyrobot.calendarworkflow

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.DrawBackground
import androidx.ui.foundation.VerticalScroller
import androidx.ui.graphics.Color
import androidx.ui.layout.Center
import androidx.ui.layout.Column
import androidx.ui.layout.LayoutHeight
import androidx.ui.layout.Row
import androidx.ui.layout.Spacer
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ripple.Ripple
import androidx.ui.text.TextStyle
import androidx.ui.unit.dp
import com.friendlyrobot.data.CalendarEvent
import com.friendlyrobot.data.CalendarName
import com.squareup.workflow.ui.compose.bindCompose
import java.text.SimpleDateFormat

@RequiresApi(Build.VERSION_CODES.O)
var hour: String? = "HH:MM"
var date: String? = "yyyy-MM-dd"

var simpleDateFormat: SimpleDateFormat = SimpleDateFormat(date)
var timeFormatter: SimpleDateFormat = SimpleDateFormat(hour)

val loadingRendering = bindCompose<LoadingWorkflow.LoadingRending> { rendering, _ ->
    MaterialTheme {
        DrawHelloRendering(rendering.loadingMessage)
    }
}

val chooseLoadingRending = bindCompose<ChooseWorkflow.Rendering.Loading> { rendering, _ ->
    MaterialTheme {
        DrawHelloRendering(rendering.message)
    }
}

val calendarRendering = bindCompose<CalendarWorkflow.Rendering.Calendar> { rendering, _ ->
    VerticalScroller {
        Column(modifier = DrawBackground(color = Color.Black)) {
            Row(modifier = DrawBackground(color = Color.Cyan)) {
                Text(
                    text = "Calendar Events",
                    style = MaterialTheme.typography().h6
                )
            }
            Spacer(modifier = LayoutHeight(16.dp))

            rendering
                .calendarEvents
                .forEach { calEvent -> CalendarItem(calEvent) }
        }
    }
}

@Composable
private fun DrawHelloRendering(loadingMessage: String) {
    Ripple(bounded = true) {
        Clickable(onClick = { }) {
            Center {
                Text(loadingMessage)
            }
        }
    }
}

@Composable
private fun CalendarItem(calendarEvent: CalendarEvent) {
    DateRow(calendarEvent)
    TitleRow(calendarEvent)
    Spacer(modifier = LayoutHeight(20.dp))
}

@Composable
private fun TitleRow(calendarEvent: CalendarEvent) {
    Row(modifier = DrawBackground(color = Color.Magenta)) {
        Text(
            text = calendarEvent.title,
            style = MaterialTheme.typography().h6
        )
    }
}

@Composable
private fun DateRow(calendarEvent: CalendarEvent) {
    Row(modifier = DrawBackground(color = Color.Black)) {
        Text(
            text = simpleDateFormat.format(calendarEvent.start) + " from " + timeFormatter.format(
                calendarEvent.start
            ) + " to " + timeFormatter.format(
                calendarEvent.end
            ),
            style = TextStyle(color = Color.Magenta)
        )
    }
}

val chooseRendering = bindCompose<ChooseWorkflow.Rendering.Calenders> { rendering, _ ->
    VerticalScroller {
        Column(modifier = DrawBackground(color = Color.Black)) {
            Row(modifier = DrawBackground(color = Color.Cyan)) {
                Text(
                    text = "Calendar Events",
                    style = MaterialTheme.typography().h6
                )
            }
            Spacer(modifier = LayoutHeight(16.dp))

            rendering
                .calendars
                .forEach { calendar -> ChooseItem(calendar, rendering.onCalendarChosen) }
        }
    }
}

@Composable
private fun ChooseItem(
    calendarName: CalendarName,
    onCalendarChosen: (calender: String) -> Unit
) {
    ChooseRow(calendarName, onCalendarChosen)
    Spacer(modifier = LayoutHeight(20.dp))
}

@Composable
private fun ChooseRow(
    calendarName: CalendarName,
    onCalendarChosen: (calender: String) -> Unit
) {
    Clickable(onClick = {onCalendarChosen(calendarName.id)}) {
        Row(modifier = DrawBackground(color = Color.Magenta)) {
            Text(
                text = calendarName.title,
                style = MaterialTheme.typography().h6
            )
        }
    }
}
