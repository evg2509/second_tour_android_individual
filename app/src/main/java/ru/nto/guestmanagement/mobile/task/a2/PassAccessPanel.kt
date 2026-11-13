package ru.nto.guestmanagement.mobile.task.a2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import ru.nto.guestmanagement.mobile.R
import ru.nto.guestmanagement.mobile.data.TimeProvider
import ru.nto.guestmanagement.mobile.model.PassData
import ru.nto.guestmanagement.mobile.model.PassStatus
import ru.nto.guestmanagement.mobile.ui.PassQr
import ru.nto.guestmanagement.mobile.ui.TestIds
import ru.nto.guestmanagement.mobile.ui.statusColor

@Composable
fun PassAccessPanel(data: PassData, timeProvider: TimeProvider, modifier: Modifier = Modifier) {
    val now = timeProvider.now()
    val status = calculatePassStatus(data.pass, data.scans, now)

    val statusText = when (status) {
        PassStatus.BLOCKED -> "Заблокирован"
        PassStatus.NOT_STARTED -> "Ещё не действует"
        PassStatus.EXPIRED -> "Истёк"
        PassStatus.EXHAUSTED -> "Лимит проходов исчерпан"
        PassStatus.ACTIVE -> "Активен"
    }

    val statusColorRes = when (status) {
        PassStatus.BLOCKED -> R.color.status_blocked
        PassStatus.NOT_STARTED -> R.color.status_not_started
        PassStatus.EXPIRED -> R.color.status_expired
        PassStatus.EXHAUSTED -> R.color.status_exhausted
        PassStatus.ACTIVE -> R.color.status_active
    }

    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = statusText,
            modifier = Modifier
                .testTag(TestIds.PASS_STATUS)
                .statusColor(colorResource(id = statusColorRes))
        )

        if (status == PassStatus.ACTIVE) {
            PassQr(content = data.pass.qrCode)
        } else {
            Text(
                text = "QR-код недоступен",
                modifier = Modifier.testTag(TestIds.PASS_QR_HIDDEN)
            )
        }
    }
}