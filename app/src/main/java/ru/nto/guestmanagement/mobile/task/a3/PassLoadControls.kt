package ru.nto.guestmanagement.mobile.task.a3

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ru.nto.guestmanagement.mobile.model.PassScreenState
import ru.nto.guestmanagement.mobile.ui.TestIds

@Composable
fun PassLoadControls(
    state: PassScreenState,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            is PassScreenState.InitialLoading -> {
                CircularProgressIndicator(modifier = Modifier.testTag(TestIds.PASS_LOADING))
            }
            is PassScreenState.InitialError -> {
                Text(text = state.message, modifier = Modifier.testTag(TestIds.PASS_ERROR))
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onRetry, modifier = Modifier.testTag(TestIds.PASS_RETRY)) {
                    Text("Повторить")
                }
            }
            is PassScreenState.Content -> {
                if (state.isRefreshing) {
                    CircularProgressIndicator(modifier = Modifier.testTag(TestIds.PASS_LOADING))
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.testTag(TestIds.PASS_REFRESH)
                    ) {
                        Text("Обновить")
                    }
                } else {
                    Button(onClick = onRefresh, modifier = Modifier.testTag(TestIds.PASS_REFRESH)) {
                        Text("Обновить")
                    }
                    if (state.isOffline) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Нет связи", modifier = Modifier.testTag(TestIds.PASS_OFFLINE))
                    }
                }
            }
        }
    }
}