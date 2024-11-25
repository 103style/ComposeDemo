package com.example.composedemo.chapter5_state

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LocalContentColor
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composedemo.ui.theme.ComposeDemoTheme
import kotlin.random.Random

/**
 * @author Created by kempluo 2024/11/24 16:59
 *
 * https://www.bilibili.com/video/BV1ob4y1a7ad
 * 第 27 - 34 节
 *
 *
 * compose 和 viewModel 结合 构造消息列表的添加和删除
 * @see TodoScreenActivity todoVM.todoItems.observeAsState
 *
 * remember函数提供了可组合函数内存，系统会将它计算的值储存在组合树中，
 * 而且只有当remember的键发生变化的时候才会重新计算改值，(可以理解为 recyclerView 的 diffUtil?)
 * key不变的时候 value不会变化
 * remember(key){
 *     value
 * }
 *
 * 有状态和无状态       👆
 * - 使用remember存储对象的可组合项会创建内部状态，使该可组合项有状态
 * - 在调用方不需要控制状态，并且不必自行管理状态便可使用状态的情况下，“有状态”会非常有用。
 *   但是，具有内部状态的可组合项往往 ⚠️不易重复使用，也更难测试⚠️
 * - 无状态可组合项 是指不保持任何状态的可组合项。实现无状态的一种简单方法是使用 ⚠️状态提升⚠️
 */
class StateDemoActivity : ComponentActivity() {

    private val todoVM by viewModels<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text("ComposeStateDemo") },
                        backgroundColor = MaterialTheme.colorScheme.primary,
                    )
                }, content = { padding ->
                    Box(Modifier.padding(padding)) {
                        TodoScreenActivity()
                    }
                })
            }
        }
    }

    @Composable
    private fun TodoScreenActivity() {
        TodoScreen(
            todoVM.todoItems,
            onAddItem = todoVM::addItem,
            onRemoveItem = todoVM::removeItem,
            currentlyEditing = todoVM.curEditTodoItem,
            onStartEdit = todoVM::onEditItemSelected,
            onEditItemChange = todoVM::onEditItemChange,
            onEditDone = todoVM::onEditDone
        )
    }
}

@Composable
private fun TodoScreen(
    dataList: List<TodoItem> = defaultDataList,
    onAddItem: (TodoItem) -> Unit,
    onRemoveItem: (TodoItem) -> Unit,
    currentlyEditing: TodoItem? = null,
    onStartEdit: (TodoItem) -> Unit = {},
    onEditItemChange: (TodoItem) -> Unit = {},
    onEditDone: () -> Unit = {},
) {
    Column {
        // 顶部的输入框是否显示编辑状态
        val enableTopSection = currentlyEditing != null

        TodoInputBackground(true) {
            if (enableTopSection) {
                TodoEditHint(Modifier.align(Alignment.CenterVertically))
            } else {
                TodoInputDemo(onAddItem)
            }
        }
        // 多行
        LazyColumn(
            modifier = Modifier.weight(1f), contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(dataList) { todo ->
                if (todo.id == currentlyEditing?.id) {
                    TodoItemInlineEditor(
                        todo,
                        onEditItemChange = onEditItemChange,
                        onEditDone = onEditDone,
                        onRemoveItem = onRemoveItem
                    )
                } else {
                    TodoItemView(
                        todo,
                        onItemClick = { onStartEdit(todo) },
                        Modifier.fillParentMaxWidth(),
                    )
                }
            }
        }

        Button(
            onClick = {
                onAddItem(generateRandomTodoItem())
            }, modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("add random todo")
        }
    }
}

/**
 * 顶部的编辑态提示
 */
@Composable
fun TodoEditHint(modifier: Modifier = Modifier) {
    Text(
        "Editing  Item",
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}


@Composable
private fun TodoItemView(
    item: TodoItem, onItemClick: (TodoItem) -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier
            .clickable {
                onItemClick(item)
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            item.task,
            Modifier
                .padding(end = 8.dp)
                .weight(1f)
        )
        // item.id 不变的时候 iconAlpha不会变
        val iconAlpha = remember(item.id) { randomTint() }
        Icon(
            item.icon.imageVector, stringResource(item.icon.contentDescription),
            // 直接使用 randomTint() 会导致每次列表更新的时候 alpha发生变化
            // tint = LocalContentColor.current.copy(alpha = randomTint())
            tint = LocalContentColor.current.copy(alpha = iconAlpha),
        )
    }
}

/**
 * 生成一个随机的透明度
 */
private fun randomTint(): Float {
    return Random.nextFloat().coerceIn(0.3f, 0.9f)
}