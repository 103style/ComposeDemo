package com.example.composedemo.chapter5_state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class TodoViewModel : ViewModel() {

    // todoItems 只读集合
    var todoItems = mutableStateListOf<TodoItem>()
        private set

    // 编辑的索引位置
    private var currentEditIndex by mutableStateOf(-1)

    // 当前正在编辑的对象
    val curEditTodoItem: TodoItem?
        get() = todoItems.getOrNull(currentEditIndex)


    fun addItem(item: TodoItem) {
        todoItems.add(item)
    }

    fun removeItem(item: TodoItem) {
        todoItems.remove(item)
        onEditDone()
    }

    fun onEditDone() {
        currentEditIndex = -1
    }


    fun onEditItemSelected(item: TodoItem) {
        currentEditIndex = todoItems.indexOf(item)
    }


    fun onEditItemChange(item: TodoItem) {
        todoItems[currentEditIndex] = item
    }

}