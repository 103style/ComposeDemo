package com.example.composedemo.chapter5_state

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.composedemo.ui.theme.ComposeDemoTheme

class CustomLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text("ComposeDemos") },
                        backgroundColor = MaterialTheme.colorScheme.primary,

                        )
                }, content = { padding ->
                    Box(Modifier.padding(padding)) {
                        TodoScreen()
                    }
                })
            }
        }
    }
}

@Composable
private fun TodoScreen(dataList: List<TodoItem> = defaultDataList) {
    Column {
        // 多行
        LazyColumn(
            modifier = Modifier.weight(1f), contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(dataList) {
                TodoItemView(it, Modifier.fillParentMaxWidth())
            }
        }

        Button(onClick = {

        }, modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()) {
            Text("add random todo")
        }
    }
}

@Composable
private fun TodoItemView(item: TodoItem, modifier: Modifier = Modifier) {
    Row(
        modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(item.task, Modifier.padding(4.dp))

        Icon(
            item.icon.imageVector,
            stringResource(item.icon.contentDescription),
        )
    }
}