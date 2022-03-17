package com.task.timerapp

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.task.timerapp.timer.TimerViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private lateinit var timerViewModel: TimerViewModel


    @Before
    fun setUp() {
        timerViewModel = TimerViewModel()
        composeTestRule.setContent {
            val scaffoldState = rememberScaffoldState()
            Scaffold(scaffoldState = scaffoldState) {
                TimersScreen(timerViewModel, scaffoldState)
            }
        }
    }

    @Test
    fun validate_fields_exits() {
        composeTestRule.onNodeWithText("hours").assertExists()
        composeTestRule.onNodeWithText("Start!").assertExists()
    }

    @Test
    fun validate_start_button_click_with_normal_text() {
        timerViewModel.hour.value = "2"
        timerViewModel.min.value = "4"
        timerViewModel.hour.value = "12"
        composeTestRule.onNodeWithText("Start!").performClick()
    }

    @Test
    fun validate_start_button_click_with_string_values() {
        timerViewModel.hour.value = "2"
        timerViewModel.min.value = "test"
        timerViewModel.hour.value = "12"
        composeTestRule.onNodeWithText("Start!").performClick()
    }
}