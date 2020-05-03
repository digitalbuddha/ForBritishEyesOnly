package com.friendlyrobot.calendarworkflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.friendlyrobot.data.provideCalendarStore
import com.friendlyrobot.data.provideEventStore
import com.squareup.workflow.diagnostic.SimpleLoggingDiagnosticListener
import com.squareup.workflow.ui.ViewRegistry
import com.squareup.workflow.ui.WorkflowRunner
import com.squareup.workflow.ui.modal.AlertContainer
import com.squareup.workflow.ui.setContentWorkflow

private val viewRegistry = ViewRegistry(
    calendarRendering,
    loadingRendering,
    chooseRendering,
    chooseLoadingRending,
    AlertContainer
)

class HelloWorkflowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val calendarWorkflow =
            CalendarWorkflow(provideEventStore(applicationContext), LoadingWorkflow)
        val chooseWorkflow =
            ChooseWorkflow(provideCalendarStore(applicationContext), LoadingWorkflow)
        val mainWorkflow = MainWorkflow(calendarWorkflow, chooseWorkflow)

        setContentWorkflow(viewRegistry) {
            WorkflowRunner.Config(
                mainWorkflow,
                diagnosticListener = SimpleLoggingDiagnosticListener()
            )
        }
    }

    // To opt-in to state saving, use this alternative and
    // implement StatefulWorkflow.snapshotState.
//
//    private lateinit var runner: WorkflowRunner<Unit>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        runner = setContentWorkflow(viewRegistry) {
//            WorkflowRunner.Config(HelloWorkflow, Unit)
//        }
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        runner.onSaveInstanceState(outState)
//    }
//

    /**
     * To handle the back button, uncomment this and have your
     * [LayoutRunners][com.squareup.workflow.ui.LayoutRunner] use
     * [setBackHandler][com.squareup.workflow.ui.setBackHandler].
     */
//    override fun onBackPressed() {
//        if (!workflowOnBackPressed()) super.onBackPressed()
//    }
}
