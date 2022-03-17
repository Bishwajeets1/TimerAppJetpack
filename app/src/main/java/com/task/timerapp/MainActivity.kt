package com.task.timerapp


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.task.timerapp.timer.TimerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val timerViewModel: TimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scaffoldState = rememberScaffoldState()
            Scaffold(scaffoldState = scaffoldState) {
                TimersScreen(timerViewModel, scaffoldState)
            }

        }
    }

    override fun onDestroy() {
        if (isChangingConfigurations.not())
            timerViewModel.stopTimer()
        super.onDestroy()
    }
}


@Composable
fun TimersScreen(timerViewModel: TimerViewModel, scaffoldState: ScaffoldState) {
    Surface {
        Column(modifier = Modifier.padding(16.dp)) {
            NewTimer(timerViewModel, scaffoldState)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Running timers:")
            Spacer(modifier = Modifier.height(8.dp))
            TimerLists(timerViewModel)

        }
    }
}

@Composable
fun TimerLists(timerViewModel: TimerViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(timerViewModel.timersList.reversed()) { timer ->
            Divider()
            Timer(timer)
        }
    }
}

@Composable
fun NewTimer(timerViewModel: TimerViewModel, scaffoldState: ScaffoldState) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy(8.dp)) {
        TimeInput(placeholderText = "hours", modifier = Modifier.weight(1f), timerViewModel)
        TimeInput(placeholderText = "minutes", modifier = Modifier.weight(1f), timerViewModel)
        TimeInput(placeholderText = "seconds", modifier = Modifier.weight(1f), timerViewModel)
        val focusManager = LocalFocusManager.current
        val errorMessage = stringResource(id = R.string.invalid_output)
        val close = stringResource(id = R.string.close)
        Button(
            onClick = {
                focusManager.clearFocus()
                if (Validator.validateInput(
                        timerViewModel.hour.value,
                        timerViewModel.min.value,
                        timerViewModel.sec.value
                    )
                )
                    timerViewModel.updateTimerList()
                else {
                    CoroutineScope(Dispatchers.IO).launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = errorMessage,
                            actionLabel = close,
                            duration = SnackbarDuration.Short,
                        )
                    }
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(id = R.string.start))
        }
    }
}

@Composable
fun TimeInput(
    placeholderText: String,
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel
) {

    var input = timerViewModel.sec.value
    if (placeholderText == "hours") {
        input = timerViewModel.hour.value
    } else if (placeholderText == "minutes") {
        input = timerViewModel.min.value
    }

    TextField(
        value = input,
        onValueChange = {
            if (it.length > 2) {
                input = it.substring(0, 2)
                return@TextField
            }
            if (placeholderText == "hours") {
                timerViewModel.updateHour(it.trim())
            } else if (placeholderText == "minutes") {
                timerViewModel.updateMin(it.trim())
            } else {
                timerViewModel.updateSec(it.trim())
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = true, keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
        ),
        placeholder = {
            Text(
                text = placeholderText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(fontSize = 10.sp)
            )
        },
        modifier = modifier
    )
}

@Composable
fun Timer(value: String) {
    Text(
        text = value,
        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
        modifier = Modifier.padding(vertical = 16.dp)
    )
}



@Preview
@Composable
fun PreviewTimeInput() {
    TimeInput(placeholderText = "seconds", timerViewModel = TimerViewModel())
}

@Preview
@Composable
fun PreviewTimersEmpty() {
    val timerViewModel = TimerViewModel()
    val scaffoldState = rememberScaffoldState()
    TimersScreen(timerViewModel, scaffoldState)
}


@Preview
@Composable
fun PreviewTimers() {
    val timerViewModel = TimerViewModel()
    timerViewModel.updateTimerList()
    val scaffoldState = rememberScaffoldState()
    TimersScreen(timerViewModel, scaffoldState)
}


@Preview
@Composable
fun PreviewTimer() {
    Timer("03:23:14")
}

@Preview
@Composable
fun PreviewNewTimer() {
    val timerViewModel = TimerViewModel()
    timerViewModel.updateTimerList()
    val scaffoldState = rememberScaffoldState()
    NewTimer(timerViewModel, scaffoldState = scaffoldState)

}

@Preview
@Composable
fun PreviewTimerList() {
    val timerViewModel = TimerViewModel()
    timerViewModel.updateTimerList()
    TimerLists(timerViewModel)
}
