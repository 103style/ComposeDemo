package com.example.composedemo.chapter10_effect_api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @author Created by kempluo 2024/11/30 16:14
 *
 * 由于LaunchEffect 是可组合函数， 因此只能在其他可组合函数中使用。
 * 1.想要在 可组合函数之外启动协程，且需要限制协程的作用域，以便在退出组合函数后自动取消。
 * 2.需要手动控制一个或多个协程的生命周期。
 * 可以使用 rememberCoroutineScope
 */
@Composable
fun RememberCoroutineScopeDemo() {
    ScaffoldDemo()
}

@Composable
private fun ScaffoldDemo() {
    val scaffoldState = rememberScaffoldState()

    // 不是GlobalScope，因为它可以在dispose之后，自动停止协程的执行
    // 相当于LifecycleOwner 的 lifecycleScope、ViewModel 的 viewModelScope
    val scope = rememberCoroutineScope()
    //2.记录正在执行的任务
    var runningJob: Job? = null
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("RememberCoroutineScopeDemo", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = {
                        // 打开侧边栏
                        runningJob = scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = null)
                    }
                },
            )
        },
        drawerContent = {
            Box(
                Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text("你在找我吗？")
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(text = { Text("你敢点我吗！", color = Color.White) },
                onClick = {
                    if (runningJob?.isActive == true) {
                        // 3.取消之前的任务
                        runningJob?.cancel()
                    }
                    // 1. 重复点击会生成很多过提示， 依次执行，不会取消上一个
                    runningJob = scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("点我又能怎么样？")
                    }
                })
        },
        content = {
            Box(
                Modifier
                    .padding(it)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Button(onClick = {}) {
                    Text("这里什么都没有...")
                }
            }
        },
    )
}