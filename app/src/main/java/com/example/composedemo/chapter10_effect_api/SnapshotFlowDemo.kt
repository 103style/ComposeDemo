package com.example.composedemo.chapter10_effect_api

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * @author Created by kempluo 2024/11/30 23:23
 *
 * 使用snapshotFlow 可以将State对象转换为Flow。
 * snapshotFlow 会运行传入的block，并发出从块中读取的State对象的结果。
 * 当在snapshotFlow块中读取的State对象之一发生变化时，如果新值与之前发出的值不相等，Flow会向其收集器发新值。
 */
@Composable
fun SnapshotFlowDemo() {
    var lastCollectValue by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "lastCollectValue:$lastCollectValue")
        }
        LazyColumn(state = listState) {
            items(100) {
                Text(
                    text = "Item:$it",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }


    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .filter { it > 20 }
            .distinctUntilChanged()
            .collect {
                lastCollectValue = it
            }
    }
}