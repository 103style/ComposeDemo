package com.example.composedemo.chapter10_effect_api

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * @author Created by kempluo 2024/11/30 22:14
 *
 * 如果某个状态是从其他状态对象计算或派生得出的，请使用derivedStateOf。
 * 作为条件的状态我们称为条件状态。
 * 当任意一个条件状态更新时，结果状态都会重新计算。
 */
@Composable
fun DerivedStateOfDemo() {
    ToDoList()
}

@Composable
private fun ToDoList(highPriorityWords: List<String> = listOf("Review", "Compose")) {
    val todoTasks = remember {
        mutableStateListOf<String>().apply {
            add("Anim Demo")
            add("Compose Learn")
            add("Gesture Demo")
        }
    }
    val (text, setText) = remember { mutableStateOf("") }

    // 当任意一个条件状态更新时，结果状态都会重新计算。
    val highPriorityTasks = remember(todoTasks, highPriorityWords) {
        derivedStateOf {
            todoTasks.filter {
                it.containsWord(highPriorityWords)
            }
        }
    }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = text, onValueChange = setText,
                placeholder = {
                    Text("带有 Compose/Review 的为高优先级")
                },
                modifier = Modifier.weight(1f),
            )

            Button(onClick = {
                todoTasks.add(text)
                setText("")
            }, modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(text = "Add")
            }
        }

        LazyColumn {
            items(highPriorityTasks.value) {
                ItemText(it, modifier = Modifier.background(Color.LightGray))
            }
            items(todoTasks) {
                ItemText(it)
            }
        }
    }
}

private fun String.containsWord(list: List<String>): Boolean {
    list.forEach {
        if (this.contains(it)) {
            return true
        }
    }
    return false
}


@Composable
private fun ItemText(s: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = s)
    }
}
