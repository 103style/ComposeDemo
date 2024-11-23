package com.example.composedemo.chapter3_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.composedemo.ui.theme.ComposeDemoTheme
import kotlinx.coroutines.launch

class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                Box(Modifier.safeDrawingPadding()) {
                    ScrollingList()
                }
            }
        }
    }
}

@Composable
fun SimpleColumn() { // 不能滚动, 相当于LinearLayout 内部有 100个TextView
    Column {
        repeat(100) {
            Text(text = "Item #$it", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun SimpleList() {  // 相当于 scrollView 内部是LinearLayout 然后再内部有 100个TextView
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        repeat(100) {
            Text(text = "Item #$it", style = MaterialTheme.typography.titleMedium)
        }
    }
}


@Composable
fun LazyList() { // 有缓冲，相当于RecyclerView
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState, horizontalAlignment = Alignment.CenterHorizontally) {
        items(100) {
            Text(
                text = "Item #$it",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ScrollingList() { // 有缓冲，相当于RecyclerView
    val count = 100
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Column {
        Row {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(0)
                    }
                },
            ) {
                Text("Scroll to top")
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(count - 1)
                    }
                },
            ) {
                Text("Scroll to bottom")
            }
        }
        LazyColumn(state = scrollState, horizontalAlignment = Alignment.CenterHorizontally) {
            items(count) {
                ItemView(it)
            }
        }
    }
}

@Composable
fun ItemView(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = "https://pic.616pic.com/photoone/00/05/05/618e218c176bb3876.jpg",
            contentDescription = null,
            modifier = Modifier
                .width(200.dp)
                .height(100.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Item #$index",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )
    }
}