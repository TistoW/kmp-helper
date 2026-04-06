package com.tisto.kmp.helper.ui.utils.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.theme.ComposeHelperTheme
import com.tisto.kmp.helper.ui.theme.TextAppearance

/**
 * Countdown timer that counts down from specified minutes
 *
 * @param initialMinutes Starting minutes for countdown (default: 45)
 * @param onFinish Callback when countdown reaches 00:00:00
 * @return Formatted countdown string "HH:MM:SS"
 */
@Composable
fun rememberCountdownTimer(
    initialMinutes: Int = 45,
    onFinish: () -> Unit = {}
): String {
    // Convert minutes to milliseconds
    val initialTimeMillis = remember(initialMinutes) {
        TimeUnit.MINUTES.toMillis(initialMinutes.toLong())
    }

    var timeLeftMillis by remember { mutableLongStateOf(initialTimeMillis) }
    var isRunning by remember { mutableStateOf(true) }

    LaunchedEffect(isRunning) {
        while (isRunning && timeLeftMillis > 0) {
            delay(1000L) // Update every second
            timeLeftMillis -= 1000L

            if (timeLeftMillis <= 0) {
                timeLeftMillis = 0
                isRunning = false
                onFinish()
            }
        }
    }

    return formatTime(timeLeftMillis)
}

/**
 * Format milliseconds to HH:MM:SS string
 */
private fun formatTime(millis: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(millis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

/**
 * Alternative: Countdown timer with custom start time
 *
 * @param hours Starting hours
 * @param minutes Starting minutes
 * @param seconds Starting seconds
 * @param onFinish Callback when countdown reaches zero
 */
@Composable
fun rememberCustomCountdown(
    hours: Int = 0,
    minutes: Int = 45,
    seconds: Int = 0,
    onFinish: () -> Unit = {}
): CountdownState {
    val totalMillis = remember(hours, minutes, seconds) {
        (hours * 3600 + minutes * 60 + seconds) * 1000L
    }

    var timeLeftMillis by remember { mutableLongStateOf(totalMillis) }
    var isRunning by remember { mutableStateOf(true) }

    LaunchedEffect(isRunning) {
        while (isRunning && timeLeftMillis > 0) {
            delay(1000L)
            timeLeftMillis -= 1000L

            if (timeLeftMillis <= 0) {
                timeLeftMillis = 0
                isRunning = false
                onFinish()
            }
        }
    }

    val currentHours = TimeUnit.MILLISECONDS.toHours(timeLeftMillis)
    val currentMinutes = TimeUnit.MILLISECONDS.toMinutes(timeLeftMillis) % 60
    val currentSeconds = TimeUnit.MILLISECONDS.toSeconds(timeLeftMillis) % 60

    return CountdownState(
        hours = currentHours.toInt(),
        minutes = currentMinutes.toInt(),
        seconds = currentSeconds.toInt(),
        formattedTime = String.format("%02d:%02d:%02d", currentHours, currentMinutes, currentSeconds),
        isFinished = timeLeftMillis <= 0,
        totalMillis = totalMillis,
        remainingMillis = timeLeftMillis
    )
}

data class CountdownState(
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
    val formattedTime: String,
    val isFinished: Boolean,
    val totalMillis: Long,
    val remainingMillis: Long
) {
    val progressPercentage: Float
        get() = if (totalMillis > 0) {
            (remainingMillis.toFloat() / totalMillis.toFloat())
        } else 0f
}

/**
 * Extension: Get color based on time remaining
 * Red when < 5 minutes, Orange when < 15 minutes, Green otherwise
 */
fun CountdownState.getTimerColor(): Color {
    return when {
        minutes < 5 -> Color(0xFFDC2626) // Red
        minutes < 15 -> Color(0xFFEA580C) // Orange
        else -> Color(0xFF059669) // Green
    }
}

/**
 * Extension: Check if timer is in danger zone (< 5 minutes)
 */
fun CountdownState.isDangerZone(): Boolean = minutes < 5

/**
 * Extension: Check if timer is in warning zone (< 15 minutes)
 */
fun CountdownState.isWarningZone(): Boolean = minutes < 15


// EXAMPLE


/**
 * COUNTDOWN TIMER - USAGE EXAMPLES
 *
 * The countdown timer provides automatic countdown functionality
 * with customizable start time and callback when finished.
 */

// ============================================
// EXAMPLE 1: Simple 45-minute countdown
// ============================================
@Composable
fun Example1_SimpleCountdown() {
    val countdown = rememberCountdownTimer(
        initialMinutes = 45,
        onFinish = {
            // Handle countdown finish (e.g., show dialog, navigate away)
            println("Payment time expired!")
        }
    )

    Text(
        text = "Time remaining: $countdown",
        style = TextAppearance.body1()
    )
}

// ============================================
// EXAMPLE 2: Custom countdown with hours
// ============================================
@Composable
fun Example2_CustomCountdown() {
    val countdownState = rememberCustomCountdown(
        hours = 1,
        minutes = 30,
        seconds = 0,
        onFinish = {
            // Handle finish
        }
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Hours: ${countdownState.hours}")
        Text("Minutes: ${countdownState.minutes}")
        Text("Seconds: ${countdownState.seconds}")
        Text("Formatted: ${countdownState.formattedTime}")
        Text("Is Finished: ${countdownState.isFinished}")
        Text("Progress: ${countdownState.progressPercentage * 100}%")
    }
}

// ============================================
// EXAMPLE 4: Dynamic color based on time
// ============================================
@Composable
fun Example4_DynamicColor() {
    val countdown = rememberCustomCountdown(minutes = 10)

    val timerColor = countdown.getTimerColor()
    // Returns:
    // - Red if < 5 minutes remaining
    // - Orange if < 15 minutes remaining
    // - Green otherwise

    Text(
        text = countdown.formattedTime,
        color = timerColor,
        style = TextAppearance.body1()
    )
}

// ============================================
// EXAMPLE 5: Check danger zones
// ============================================
@Composable
fun Example5_DangerZones() {
    val countdown = rememberCustomCountdown(minutes = 45)

    when {
        countdown.isDangerZone() -> {
            // < 5 minutes: Show urgent warning
            Text("⚠️ URGENT: Payment expiring soon!", color = Color.Red)
        }
        countdown.isWarningZone() -> {
            // < 15 minutes: Show warning
            Text("⏰ Warning: Please complete payment", color = Color(0xFFEA580C))
        }
        else -> {
            // > 15 minutes: Normal state
            Text("Time remaining: ${countdown.formattedTime}")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PaymentCompletionPreview() {
    ComposeHelperTheme {
        Example1_SimpleCountdown()
    }
}