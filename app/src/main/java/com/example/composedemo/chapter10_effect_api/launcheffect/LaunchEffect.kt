package com.example.composedemo.chapter10_effect_api.launcheffect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * @author Created by kempluo 2024/11/30 15:16
 *
 * 当 LaunchEffect 进入组件树时，会启动一个协程，并将block放入协程中执行。
 * 当 可组合函数 从视图树上 detach 时，协程还未被执行完毕，该协程也将会被取消执行。
 * 当 LaunchEffect 在重组时其key不变，那就不会被重新启动执行block.
 * 当 LaunchEffect 在重组时其key变化了，则会先执行cancel，再重新启动一个新的协程执行block.
 */
@Composable
fun LaunchEffectDemo() {
    val state = remember { mutableStateOf(0) }
    ScaffoldDemo(state)
}

@Composable
fun ScaffoldDemo(
    state: MutableState<Int>, scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    if (state.value != 0) {
        // 默认会启动， 所以加个if条件让首次不弹提示， 只有点击操作的时候才弹
        LaunchedEffect(state.value) {
            println("LaunchedEffect run")
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Error Message", actionLabel = "Retry message"
            )
        }
    }
    println("ScaffoldDemo run, value:${state.value}")
    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(title = { Text("title", color = Color.White) })
    }, content = {
        Box(
            Modifier
                .padding(it)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                // 变更state变，触发树重组，来触发 提示弹框
                // 重复点击的时候，会发现上一个未执行完成的动画会被取消
                state.value += 1
            }) {
                Text("Error occurs")
            }
        }
    })
}