package com.friendlyrobot.calendarworkflow

import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.foundation.Clickable
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ripple.Ripple
import androidx.ui.layout.Center
import com.squareup.workflow.ui.compose.bindCompose

val loadingRendering = bindCompose<CalendarWorkflow.Rendering.LoadingRending> { rendering, _ ->
    MaterialTheme {
        DrawHelloRendering(rendering)
    }
}

val calendarRendering = bindCompose<CalendarWorkflow.Rendering.Calendar> { rendering, _ ->
    MaterialTheme {
        DrawCalRendering(rendering)
    }
}

@Composable
private fun DrawHelloRendering(rendering: CalendarWorkflow.Rendering.LoadingRending) {
    Ripple(bounded = true) {
        Clickable(onClick = {  }) {
            Center {
                Text(rendering.loadingMessage)
            }
        }
    }
}

@Composable
private fun DrawCalRendering(rendering: CalendarWorkflow.Rendering.Calendar) {
    Ripple(bounded = true) {
        Clickable(onClick = {  }) {
            Center {
                Text(rendering.calendarEvents.toString())
            }
        }
    }
}