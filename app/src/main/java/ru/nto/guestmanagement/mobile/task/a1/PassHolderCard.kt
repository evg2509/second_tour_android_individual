package ru.nto.guestmanagement.mobile.task.a1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ru.nto.guestmanagement.mobile.ui.TestIds
import ru.nto.guestmanagement.mobile.model.PassDetails
import java.time.format.DateTimeFormatter

@Composable
fun PassHolderCard(pass: PassDetails, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = pass.visitor.fullName,
            modifier = Modifier.testTag(TestIds.PASS_HOLDER)
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (!pass.visitor.organization.isNullOrBlank()) {
            Text(
                text = pass.visitor.organization,
                modifier = Modifier.testTag(TestIds.PASS_ORGANIZATION)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        val formatterDate = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
        val formatterDateTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

        val isSameDay = pass.validFrom.toLocalDate() == pass.validTo.toLocalDate()
        val periodText = if (isSameDay) {
            // \u2013 — это короткое тире без пробелов
            "Действует ${pass.validFrom.format(formatterDate)}, ${pass.validFrom.format(formatterTime)}\u2013${pass.validTo.format(formatterTime)}"
        } else {
            "Действует с ${pass.validFrom.format(formatterDateTime)} по ${pass.validTo.format(formatterDateTime)}"
        }

        Text(
            text = periodText,
            modifier = Modifier.testTag(TestIds.PASS_VALID_PERIOD)
        )
        Spacer(modifier = Modifier.height(8.dp))

        val limitText = if (pass.maxEntries == 1) {
            "Разовый пропуск"
        } else {
            "Лимит: ${pass.maxEntries} ${getPassWordForm(pass.maxEntries)}"
        }
        Text(
            text = limitText,
            modifier = Modifier.testTag(TestIds.PASS_ENTRIES_LIMIT)
        )
    }
}

private fun getPassWordForm(n: Int): String {
    val mod100 = n % 100
    val mod10 = n % 10
    return when {
        mod100 in 11..14 -> "проходов"
        mod10 == 1 -> "проход"
        mod10 in 2..4 -> "прохода"
        else -> "проходов"
    }
}