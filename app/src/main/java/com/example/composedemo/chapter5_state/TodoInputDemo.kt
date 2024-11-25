package com.example.composedemo.chapter5_state

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

/**
 * @author Created by kempluo 2024/11/24 20:41
 *
 * https://www.bilibili.com/video/BV1ob4y1a7ad
 * 第 33 & 36节
 *
 * 通过以下三种方式声明 MutableState是一个可组合对象
 * - val state = remember{ mutableStateOf(default) }
 * - var value by remember{ mutableStateOf(default) }
 * - val (value, setValue) = remember{ mutableStateOf(default) }
 */

@Composable
fun TodoInputDemo(
    onItemComplete: (TodoItem) -> Unit
) {
    // 当前输入的事项标题
    // rememberSaveable 能保证横竖屏切换等状态 修改的值不会变为初始值
    val (text, setText) = rememberSaveable {
        mutableStateOf("")
    }
    // 当前事项选中的图标
    val (icon, setIcon) = rememberSaveable {
        mutableStateOf(TodoIcon.Square)
    }
    val onSubmit = {
        // 提交， 然后重置状态
        onItemComplete(TodoItem(text, icon))
        setText("")
        setIcon(TodoIcon.Square)
    }
    val isIconRowVisible = text.isNotBlank()
    TodoEntryInput(
        text = text,
        onTextChange = {
            setText(it)
        },
        icon = icon,
        onIconChange = setIcon,
        submit = onSubmit,
        isIconRowVisible = isIconRowVisible
    ) {
        // 按钮
        TodoAddButton(
            onClick = onSubmit, text = "Add", enable = text.isNotBlank()
        )
    }
}

@Composable
fun TodoEntryInput(
    text: String,
    onTextChange: (String) -> Unit,
    icon: TodoIcon,
    onIconChange: (TodoIcon) -> Unit,
    submit: () -> Unit,
    isIconRowVisible: Boolean,
    buttonSlot: @Composable () -> Unit
) {
    Column {
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            // 输入框
            TodoInput(
                text = text,
                onTextChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                onImeAction = submit
            )
            Spacer(Modifier.width(8.dp))
            Box(Modifier.align(Alignment.CenterVertically)) { buttonSlot() }
        }

        if (isIconRowVisible) {
            InputIconRow(icon, onIconChange = onIconChange, modifier = Modifier.padding(top = 8.dp))
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}

/**
 * 第39节 编辑模式 https://www.bilibili.com/video/BV1ob4y1a7ad
 * @param item 选中的TodoItem条目
 * @param onEditItemChange 编辑条目的回调
 * @param onEditDone 编辑完成的回调
 * @param onRemoveItem 删除条目的回调
 */
@Composable
fun TodoItemInlineEditor(
    item: TodoItem,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit,
    onRemoveItem: (TodoItem) -> Unit
) {
    TodoEntryInput(
        text = item.task,
        onTextChange = { onEditItemChange(item.copy(task = it)) },
        icon = item.icon,
        onIconChange = {
            onEditItemChange(item.copy(icon = it))
        },
        submit = onEditDone,
        isIconRowVisible = true
    ) {
        // 保存 和 删除 图表
        Row {
            val shrinkButtons = Modifier.widthIn(20.dp)
            IconButton(onClick = onEditDone, modifier = shrinkButtons) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "",
                    Modifier.align(Alignment.CenterVertically)
                )
            }

            Spacer(Modifier.width(8.dp))

            IconButton(onClick = {
                onRemoveItem(item)
            }, modifier = shrinkButtons) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "",
                    Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }

}


//  第38节 输入框添加一个背景 https://www.bilibili.com/video/BV1ob4y1a7ad
@Composable
fun TodoInputBackground(
    elevate: Boolean, modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit
) {
    // 帧动画的形式展示 surface 底部的阴影
    val elevationAnim by animateDpAsState(
        if (elevate) {
            1.dp
        } else {
            0.dp
        }, TweenSpec(300)
    )
    Surface(
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
        shape = RectangleShape,
        elevation = elevationAnim
    ) {
        Row(modifier.animateContentSize(TweenSpec(300)), content = content)
    }
}

// 输入框
@Composable
fun TodoInput(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onImeAction: () -> Unit = {} // 软键盘点击完成
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = text,
        onValueChange = onTextChange,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        maxLines = 1,
        modifier = modifier,
        // 第37节 配置键盘 https://www.bilibili.com/video/BV1ob4y1a7ad
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
            keyboardController?.hide()
        })
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

/**
 * 一排图标，根本文本框是否有内容，自动收起和弹出，带动画效果
 * https://www.bilibili.com/video/BV1ob4y1a7ad
 * 第 33 & 36节
 */
@Composable
fun InputIconRow(
    icon: TodoIcon,
    onIconChange: (TodoIcon) -> Unit,
    visible: Boolean = true,
    modifier: Modifier = Modifier
) {
    // 进出动画
    val enterAnim =
        remember { fadeIn(animationSpec = TweenSpec(300, easing = FastOutLinearInEasing)) }
    val exitAnim =
        remember { fadeOut(animationSpec = TweenSpec(300, easing = FastOutSlowInEasing)) }
    Box(modifier.defaultMinSize(minHeight = 16.dp)) {
        AnimatedVisibility(visible, enter = enterAnim, exit = exitAnim) {
            IconRow(icon, onIconChange)
        }
    }
}

@Composable
fun IconRow(icon: TodoIcon, onIconChange: (TodoIcon) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier) {
        for (todoIcon in TodoIcon.entries) {
            SelectableIconButton(
                todoIcon, onIconSelected = {
                    onIconChange(todoIcon)
                }, isSelected = todoIcon == icon
            )
        }
    }
}

@Composable
fun SelectableIconButton(
    icon: TodoIcon, onIconSelected: () -> Unit, isSelected: Boolean, modifier: Modifier = Modifier
) {
    // 图标状态颜色
    val tint = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    TextButton(onClick = onIconSelected, shape = CircleShape, modifier = modifier) {
        Column {
            Icon(
                imageVector = icon.imageVector,
                contentDescription = stringResource(icon.contentDescription)
            )
            if (isSelected) {
                Box(
                    modifier
                        .padding(top = 3.dp)
                        .width(icon.imageVector.defaultWidth) // 图标的大小
                        .height(1.dp)
                        .background(tint)
                )
            }
        }
    }
}