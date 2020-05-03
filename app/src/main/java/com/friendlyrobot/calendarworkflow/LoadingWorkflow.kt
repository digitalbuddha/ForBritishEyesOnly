package com.friendlyrobot.calendarworkflow

import com.squareup.workflow.RenderContext
import com.squareup.workflow.StatelessWorkflow
import com.squareup.workflow.ui.modal.AlertContainerScreen

abstract class RenderingWorkflow<PropsT, RenderingT> :
    StatelessWorkflow<PropsT, Nothing, RenderingT>()

object LoadingWorkflow : RenderingWorkflow<Message, AlertContainerScreen<Screen>>() {
    data class LoadingRending(val loadingMessage: String = "Loading") :
        Screen

    override fun render(
        props: Message,
        context: RenderContext<Nothing, Nothing>
    ): AlertContainerScreen<Screen> {
        return AlertContainerScreen(
            LoadingRending(
                props
            )
        )
    }
}