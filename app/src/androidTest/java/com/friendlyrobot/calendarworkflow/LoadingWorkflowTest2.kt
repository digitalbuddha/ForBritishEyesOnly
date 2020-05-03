package com.friendlyrobot.calendarworkflow

import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.squareup.workflow.testing.testFromStart
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoadingWorkflowTest2 {
    @Test
    fun verifyLoadingflow2() {
        LoadingWorkflow.testFromStart("Hello"){
            val awaitNextRendering = awaitNextRendering().beneathModals
            assertThat(awaitNextRendering).isInstanceOf(LoadingWorkflow.LoadingRending::class.java)
        }
    }
}