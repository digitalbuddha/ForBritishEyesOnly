package com.friendlyrobot.calendarworkflow

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.get
import com.friendlyrobot.calendarworkflow.CalendarWorkflow.State
import com.friendlyrobot.data.CalendarEvent
import com.friendlyrobot.data.CalendarId
import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow
import com.squareup.workflow.StatelessWorkflow
import com.squareup.workflow.Worker
import com.squareup.workflow.renderChild
import com.squareup.workflow.ui.modal.AlertContainerScreen

interface Screen

typealias Message = String

class CalendarWorkflow (private val eventStore: Store<CalendarId, List<CalendarEvent>>,
    private val loadingWorkflow: LoadingWorkflow) :
    StatefulWorkflow<CalendarId, State, Nothing,  AlertContainerScreen<Screen>>() {

    sealed class State {
        object Loading : State()
        internal data class DisplayingEvents(val calendarEvents: List<CalendarEvent>) : State()
    }

    sealed class Rendering:Screen {
        data class Calendar(val calendarEvents: List<CalendarEvent>) : Rendering()
    }

    override fun initialState(
        props: CalendarId,
        snapshot: Snapshot?
    ): State = State.Loading

    override fun render(
        props: CalendarId,
        state: State,
        context: RenderContext<State, Nothing>
    ):  AlertContainerScreen<Screen> {

        return when (state) {
            is State.Loading -> {
                context.runningWorker(Worker.from {
                    eventStore.get(props)
                }) { action { nextState = State.DisplayingEvents(it) } }
                context.renderChild(loadingWorkflow,"Loading Events")
            }
            is State.DisplayingEvents -> Rendering.Calendar(state.calendarEvents).asAlertScreen()
        }
    }

    override fun snapshotState(state: State): Snapshot = Snapshot.of(state.toString())
}
