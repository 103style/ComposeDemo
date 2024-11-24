package com.example.composedemo.chapter5_state

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * @author Created by kempluo 2024/11/24 20:41
 *
 * https://www.bilibili.com/video/BV1ob4y1a7ad
 * 第 33 节
 *
 * 通过以下三种方式声明 MutableState是一个可组合对象
 * - val state = remember{ mutableStateOf(default) }
 * - var value by remember{ mutableStateOf(default) }
 * - val (value, setValue) = remember{ mutableStateOf(default) }
 */
@Composable
fun TodoInputDemo(onItemComplete: (TodoItem) -> Unit) {
    val (text, setText) = remember {
        mutableStateOf("")
    }
    Column {
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            // 输入框
            TodoInput(
                text = text,
                onTextChange = setText,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            // 按钮
            TodoAddButton(
                onClick = {
                    // 提交， 然后清空
                    onItemComplete(TodoItem(text, TodoIcon.entries.toTypedArray().random()))
                    setText("")
                },
                text = "Add",
                modifier = Modifier.align(Alignment.CenterVertically),
                enable = text.isNotBlank()
            )
        }
    }
}

// 输入框
@Composable
fun TodoInput(text: String, onTextChange: (String) -> Unit, modifier: Modifier = Modifier) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        maxLines = 1,
        modifier = modifier
    )
}


// 输入框的按钮
@Composable
fun TodoAddButton(
    onClick: () -> Unit, text: String, modifier: Modifier = Modifier, enable: Boolean = true
) {
    TextButton(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(),
        modifier = modifier,
        enabled = enable
    ) {
        Text(
            text, color = if (enable) {
                Color.White
            } else {
                Color.Gray
            }
        )
    }
}









