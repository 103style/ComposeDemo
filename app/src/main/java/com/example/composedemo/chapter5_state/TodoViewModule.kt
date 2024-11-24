package com.example.composedemo.chapter5_state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodoViewModule : ViewModel() {

    private var _todoItems = MutableLiveData<List<TodoItem>>(arrayListOf())
    var todoItems: LiveData<List<TodoItem>> = _todoItems

    fun addItem(item: TodoItem) {
        _todoItems.value = (_todoItems.value ?: arrayListOf()) + listOf(item)
    }

    fun removeItem(item: TodoItem) {
        _todoItems.value = _todoItems.value?.toMutableList()?.also {
            it.remove(item)
        }
    }
}