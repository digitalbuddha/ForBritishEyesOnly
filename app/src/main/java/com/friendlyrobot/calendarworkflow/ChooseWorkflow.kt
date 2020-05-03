package com.friendlyrobot.calendarworkflow

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.get
import com.friendlyrobot.calendarworkflow.ChooseWorkflow.State
import com.friendlyrobot.data.CalendarName
import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow
import com.squareup.workflow.Worker
import com.squareup.workflow.WorkflowAction
import com.squareup.workflow.WorkflowAction.Companion.toString
import com.squareup.workflow.renderChild
import com.squareup.workflow.ui.modal.AlertContainerScreen

class ChooseWorkflow(
    private val calendarStore: Store<Unit, Set<CalendarName>>,
    private val loadingWorkflow: LoadingWorkflow
) :
    StatefulWorkflow<Unit, State, String, AlertContainerScreen<Screen>>() {
    sealed class State {
        object LoadingCalendars : State()
        data class ShowingCalendars(val calendars: Set<CalendarName>) : State()
    }

    sealed class Rendering : Screen {
        data class Loading(val message: String = "Loading List of Calendars") : Rendering()
        data class Calenders(
            val calendars: Set<CalendarName>,
            val onCalendarChosen: (calender: String) -> Unit = { _ -> }
        ) : Rendering()
    }

    override fun initialState(
        props: Unit,
        snapshot: Snapshot?
    ): State = State.LoadingCalendars

    override fun render(
        props: Unit,
        state: State,
        context: RenderContext<State, String>
    ): AlertContainerScreen<Screen> {
        return when (state) {
            is State.LoadingCalendars -> {
                context.runningWorker(Worker.from {
                    calendarStore.get(Unit)
                }) { action { nextState = State.ShowingCalendars(it) } }
                context.renderChild(loadingWorkflow, "Loading Events")
            }
            is State.ShowingCalendars -> Rendering.Calenders(
                calendars = state.calendars,
                onCalendarChosen = { context.actionSink.send(action { setOutput(it) }) }
            ).asAlertScreen()
        }
    }

    override fun snapshotState(state: State): Snapshot = Snapshot.of(state.toString())
}

fun <PropsT, StateT, OutputT : Any, RenderingT>
    StatefulWorkflow<PropsT, StateT, OutputT, RenderingT>.action(
    name: String = "",
    update: WorkflowAction.Updater<StateT, OutputT>.() -> Unit
) = action({ name }, update)

/**
 * Convenience to create a [WorkflowAction] with parameter types matching those
 * of the receiving [StatefulWorkflow]. The action will invoke the given [lambda][update]
 * when it is [applied][WorkflowAction.apply].
 *
 * @param name Function that returns a string describing the update for debugging, included
 * in [toString].
 * @param update Function that defines the workflow update.
 */
fun <PropsT, StateT, OutputT : Any, RenderingT>
    StatefulWorkflow<PropsT, StateT, OutputT, RenderingT>.action(
    name: () -> String,
    update: WorkflowAction.Updater<StateT, OutputT>.() -> Unit
): WorkflowAction<StateT, OutputT> = object : WorkflowAction<StateT, OutputT> {
    override fun WorkflowAction.Updater<StateT, OutputT>.apply() = update.invoke(this)
    override fun toString(): String = "action(${name()})-${this@action}"
}

inline fun <reified T, StateT, reified OutputT : Any> RenderContext<StateT, OutputT>.run(
    noinline block: suspend () -> T,
    noinline handler: (T) -> WorkflowAction<StateT, OutputT>
) {
    runningWorker(Worker.from { block.invoke() }, handler = handler)
}


