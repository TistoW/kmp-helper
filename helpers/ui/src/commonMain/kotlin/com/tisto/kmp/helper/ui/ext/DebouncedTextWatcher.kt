package com.tisto.kmp.helper.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.text.isNotEmpty

// commonMain
class DebouncedTextWatcher(
    private val delay: Long = 750L,
    private val onDebounce: (String) -> Unit
) {
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    fun onTextChanged(newText: String) {
        job?.cancel()
        if (newText.isNotEmpty()) {
            job = scope.launch {
                delay(delay)
                onDebounce(newText)
            }
        } else {
            onDebounce(newText)
        }
    }

    fun cancel() {
        job?.cancel()
    }
}

@Composable
fun rememberDebouncedTextWatcher(
    delay: Long = 750L,
    onDebounce: (String) -> Unit
): DebouncedTextWatcher {
    return remember(delay) {
        DebouncedTextWatcher(delay, onDebounce)
    }
}
