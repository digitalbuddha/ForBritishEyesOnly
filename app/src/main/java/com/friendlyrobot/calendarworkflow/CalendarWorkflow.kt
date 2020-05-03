package com.friendlyrobot.calendarworkflow

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.get
import com.friendlyrobot.calendarworkflow.CalendarWorkflow.Rendering
import com.friendlyrobot.calendarworkflow.CalendarWorkflow.State
import com.friendlyrobot.data.CalendarEvent
import com.friendlyrobot.data.CalendarId
import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow
import com.squareup.workflow.Worker

class CalendarWorkflow constructor(private val eventStore: Store<CalendarId, List<CalendarEvent>>) :
    StatefulWorkflow<CalendarId, State, Nothing, Rendering>() {

    sealed class State {
        object Loading : State()
        internal data class DisplayingEvents(val calendarEvents: List<CalendarEvent>) : State()
    }

    sealed class Rendering {
        data class LoadingRending(val loadingMessage: String = "Loading") : Rendering()
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
    ): Rendering {

        return when (state) {
            is State.Loading -> {
                context.runningWorker(Worker.from {
                    eventStore.get(props)
                }) { action { nextState = State.DisplayingEvents(it) } }
                Rendering.LoadingRending()
            }
            is State.DisplayingEvents -> Rendering.Calendar(state.calendarEvents)
        }
    }

    override fun snapshotState(state: State): Snapshot = Snapshot.of(state.toString())
}
