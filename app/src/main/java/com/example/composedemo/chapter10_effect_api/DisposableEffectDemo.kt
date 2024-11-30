package com.example.composedemo.chapter10_effect_api

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * @author Created by kempluo 2024/11/30 18:02
 * DisposableEffect 是一个可组合函数，当它在其key值变化 或者 组合函数离开树时，会取消之前启动的协程，
 * 并在会取消协程前调用其回收方法进行资源回收相关的操作。
 */
@Composable
fun DisposableEffectDemo(backDispatcher: OnBackPressedDispatcher? = null) {
    var addBackCallback by remember { mutableStateOf(false) }

    var stateShowText by remember { mutableStateOf("") }
    var stateChangeCount by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = addBackCallback, onCheckedChange = {
                addBackCallback = !addBackCallback
            })
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (addBackCallback) {
                    "added back callback"
                } else {
                    "not add back callback"
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        println("-------$stateChangeCount")

        Text(
            text = if (stateChangeCount > 0) {
                "$stateShowText - state change count:$stateChangeCount"
            } else {
                stateShowText
            }
        )
    }

    // 如果显示BackHandler的时候，将addBackCallback 变为false
    // 会触发 BackHandler 中 DisposableEffect 的 onDispose
    if (addBackCallback) {
        BackHandler(backDispatcher, onBack = {
            stateChangeCount += 1
            stateShowText = "onBack"
        }, onCallDispose = {
            stateChangeCount++
            stateShowText = "onCallDispose"
        })
    }
}

@Composable
fun BackHandler(
    backDispatcher: OnBackPressedDispatcher?,
    onBack: () -> Unit,
    onCallDispose: () -> Unit,
) {
    // 更新最后执行的函数
    val curOnBack by rememberUpdatedState(onBack)
    val curOnDispose by rememberUpdatedState(onCallDispose)

    // remember 避免重复创建对象
    val backCB = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                curOnBack()
            }
        }
    }

    DisposableEffect(backDispatcher) {
        println("------- DisposableEffect run")
        backDispatcher?.addCallback(backCB)
        onDispose {
            println("------- DisposableEffect onDispose")
            backCB.remove()
            curOnDispose()
        }
    }
}