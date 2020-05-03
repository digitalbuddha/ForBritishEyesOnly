package com.friendlyrobot.calendarworkflow

import android.Manifest
import androidx.test.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.dropbox.android.external.store4.get
import com.friendlyrobot.data.provideCalendarStore
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class CalendarStoreTest {
    @Rule
    @JvmField
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.READ_CALENDAR)

    @Test
    fun useAppContext() = runBlocking {
        val appContext = InstrumentationRegistry.getTargetContext()
        val calendarStore = provideCalendarStore(appContext)
        val result = calendarStore.get(Unit)
        assertThat(result).isNotEmpty()
    }
}
