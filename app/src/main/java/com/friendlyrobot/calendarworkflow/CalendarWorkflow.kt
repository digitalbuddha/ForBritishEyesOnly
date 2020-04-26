package com.friendlyrobot.calendarworkflow

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.get
import com.friendlyrobot.calendarworkflow.CalendarWorkflow.Rendering
import com.friendlyrobot.calendarworkflow.CalendarWorkflow.State
import com.friendlyrobot.data.CalendarEvent
import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow
import com.squareup.workflow.Worker
import com.squareup.workflow.WorkflowAction
import kotlinx.coroutines.delay

class CalendarWorkflow constructor(private val calendarStore: Store<Unit, List<CalendarEvent>>) :
    StatefulWorkflow<Unit, State, Nothing, Rendering>() {

    sealed class State {
        internal data class Loading(val isLoaded: Boolean = false) : State()
        internal data class DisplayingEvents(val calendarEvents: List<CalendarEvent>) : State()
    }

    sealed class Rendering {
        data class LoadingRending(val loadingMessage: String = "Loading") : Rendering()
        data class Calendar(val calendarEvents: List<CalendarEvent>) : Rendering()
    }

    private sealed class Action : WorkflowAction<State, Nothing> {
        override fun WorkflowAction.Updater<State, Nothing>.apply() {
            nextState = when (this@Action) {
                is LoadCalendarEvents -> State.Loading(true)
                is ShowEvent -> State.DisplayingEvents(calendarEvents)
            }
        }

        data class ShowEvent(val calendarEvents: List<CalendarEvent>) : Action()
        object LoadCalendarEvents : Action()
    }

    override fun initialState(
        props: Unit,
        snapshot: Snapshot?
    ): State = State.Loading(isLoaded = false)

    override fun render(
        props: Unit,
        state: State,
        context: RenderContext<State, Nothing>
    ): Rendering {

        return when (state) {
            is State.Loading -> {
                context.runningWorker(Worker.from {
                    delay(5000)
                    calendarStore.get(Unit)
                }) { Action.ShowEvent(it) }
                Rendering.LoadingRending()
            }
            is State.DisplayingEvents -> Rendering.Calendar(state.calendarEvents)
        }
    }

    override fun snapshotState(state: State): Snapshot = Snapshot.of(state.toString())
}
