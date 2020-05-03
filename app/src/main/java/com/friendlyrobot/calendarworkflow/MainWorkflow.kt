package com.friendlyrobot.calendarworkflow

import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow
import com.squareup.workflow.renderChild
import com.squareup.workflow.ui.modal.AlertContainerScreen

class MainWorkflow constructor(
    private val calendarWorkflow: CalendarWorkflow,
    private val chooseWorkflow: ChooseWorkflow
) : StatefulWorkflow<Unit, MainWorkflow.MainState, Nothing, AlertContainerScreen<Screen>>() {

    sealed class MainState {
        object ChoosingCalendar : MainState()
        data class ShowingEvents(val calendarId: String) : MainState()
    }

    override fun initialState(
        props: Unit,
        snapshot: Snapshot?
    ): MainState = MainState.ChoosingCalendar

    override fun render(
        props: Unit,
        state: MainState,
        context: RenderContext<MainState, Nothing>
    ): AlertContainerScreen<Screen> {
        return when (state) {
            is MainState.ChoosingCalendar -> context.renderChild(chooseWorkflow) {
                action {
                    nextState = MainState.ShowingEvents(
                        it
                    )
                }
            }
            is MainState.ShowingEvents -> context.renderChild(
                calendarWorkflow,
                state.calendarId
            )

        }
    }

    override
    fun snapshotState(state: MainState): Snapshot = Snapshot.of(state.toString())
}

fun Screen.asAlertScreen() = AlertContainerScreen(this)
