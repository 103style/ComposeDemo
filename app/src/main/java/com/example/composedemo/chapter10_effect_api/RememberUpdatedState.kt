package com.example.composedemo.chapter10_effect_api

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * @author Created by kempluo 2024/11/30 16:57
 * LaunchedEffect 的key值更新 就会重新启动。
 * 但是有时候需要使用最新的参数值，又不想重新启动LaunchEffect,
 * 就需要用 rememberUpdatedState。
 * 它的作用是给某一个参数创建一个引用来跟踪这些参数，并保证其值被使用时是最新值，参数改变时不重启effect。
 */
@Composable
fun RememberUpdatedStateDemo() {
    val state = remember { mutableStateOf("") }

    val timeout1: () -> Unit = { state.value = "timeout1 finish" }
    val timeout2: () -> Unit = { state.value = "timeout2 finish" }

    val timeoutState = remember {
        mutableStateOf(timeout1)
    }

    val timeout = 10
    Column(Modifier.padding(16.dp)) {
        Button(onClick = {
            if (timeoutState.value != timeout1) {
                timeoutState.value = timeout1
            } else {
                timeoutState.value = timeout2
            }
        }) {
            val showText = if (timeoutState.value == timeout1) {
                "choose timeout 1, click to change"
            } else {
                "choose timeout 2, click to change"
            }
            Text(showText, modifier = Modifier.padding(16.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(state.value, modifier = Modifier.padding(16.dp))

        LandingScreen(
            timeout = timeout,
            onTimeout = timeoutState.value,
            onUpdate = {
                val showText = if (timeoutState.value == timeout1) {
                    "choose timeout 1, ${timeout}s超时"
                } else {
                    "choose timeout 2, ${timeout}s超时"
                }
                state.value = "$showText - ${timeout - it}"
            },
        )
    }
}

@Composable
private fun LandingScreen(timeout: Int, onTimeout: () -> Unit, onUpdate: (Int) -> Unit) {
    // 看起来就是记住 最后要执行的函数, 点击只是切换调用函数，不重新执行LaunchedEffect
    val curOnTimeout by rememberUpdatedState(onTimeout)

    LaunchedEffect(Unit) {
        repeat(timeout) {
            delay(1000L)
            onUpdate(it)
        }
        curOnTimeout()
    }
}
