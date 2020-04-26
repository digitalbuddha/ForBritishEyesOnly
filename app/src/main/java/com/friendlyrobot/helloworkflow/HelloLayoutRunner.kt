package com.friendlyrobot.helloworkflow

import android.view.View
import android.widget.TextView
import com.squareup.workflow.ui.ContainerHints
import com.squareup.workflow.ui.LayoutRunner
import com.squareup.workflow.ui.LayoutRunner.Companion.bind
import com.squareup.workflow.ui.ViewBinding

class LoadingLayoutRunner(view: View) : LayoutRunner<CalendarWorkflow.Rendering.LoadingRending> {
    private val messageView: TextView = view.findViewById(R.id.hello_message)

    override fun showRendering(
        rendering: CalendarWorkflow.Rendering.LoadingRending,
        containerHints: ContainerHints
    ) {
        messageView.text = "I'm Loading Calendar Events"
    }

    companion object : ViewBinding<CalendarWorkflow.Rendering.LoadingRending> by bind(
        R.layout.hello_goodbye_layout, ::LoadingLayoutRunner
    )
}

class CalendarLayoutRunner(view: View) : LayoutRunner<CalendarWorkflow.Rendering.Calendar> {
    private val messageView: TextView = view.findViewById(R.id.hello_message)

    override fun showRendering(
        rendering: CalendarWorkflow.Rendering.Calendar,
        containerHints: ContainerHints
    ) {
        messageView.text = rendering.calendarEvents.toString()
    }

    companion object : ViewBinding<CalendarWorkflow.Rendering.Calendar> by bind(
        R.layout.hello_goodbye_layout, ::CalendarLayoutRunner
    )
}
