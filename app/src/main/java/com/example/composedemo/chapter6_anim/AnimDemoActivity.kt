package com.example.composedemo.chapter6_anim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composedemo.R
import com.example.composedemo.ui.theme.ComposeDemoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author Created by kempluo 2024/11/26 15:18
 */
class AnimDemoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                Content()
            }
        }
    }

}


@Composable
fun Content() {
    val (tab, setTab) = rememberSaveable {
        mutableStateOf(TabPage.Home)
    }

    val coroutineScope = rememberCoroutineScope()

    val lazyListState = rememberLazyListState()

    val (weatherLoading, setWeatherLoading) = rememberSaveable {
        mutableStateOf(false)
    }
    val (expandedTopic, setExpandTopic) = rememberSaveable {
        mutableStateOf<DataItem?>(null)
    }
    val (editMessageShown, setEditShow) = rememberSaveable {
        mutableStateOf(false)
    }

    val tasks = remember { mutableStateListOf(testTasks) }
    val topics = remember { mutableStateListOf(testTopics) }

    suspend fun loadWeather() {
        if (!weatherLoading) {
            setWeatherLoading(true)
            delay(3000L)
            setWeatherLoading(false)
        }
    }

    suspend fun showEditMessage() {
        if (!editMessageShown) {
            setEditShow(true)
            delay(3000L)
            setEditShow(false)
        }
    }

    LaunchedEffect(Unit) {
        loadWeather()
    }

    Scaffold(
        topBar = {
            HomeTabBar(bgColor = tab.bgColor, tabPage = tab, onTabSelected = {
                setTab(it)
            })
        },
        containerColor = tab.bgColor, contentColor = tab.bgColor,
        floatingActionButton = {
            HomeFloatingActionButton(extend = false, onClick = {
                coroutineScope.launch {
                    showEditMessage()
                }
            })
        },
        content = { padding ->
            Box(Modifier.padding(padding)) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    state = lazyListState
                ) {
                    item { Header(title = stringResource(R.string.weather)) }
                    item { Spacer(Modifier.height(16.dp)) }
                    item {
                        Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 2.dp) {
                            if (weatherLoading) {
                                LoadingRow()
                            } else {
                                WeatherRow(onRefresh = {
                                    coroutineScope.launch {
                                        loadWeather()
                                    }
                                })
                            }
                        }
                    }
                    item { Spacer(Modifier.height(32.dp)) }

                    item { Header(title = stringResource(R.string.topics)) }

                    items(topics) { topic ->
                        TopicRow {
                            item = topic,
                            expanded = expandedTopic == topic,
                            onClick = {
                                expandedTopic = !expandedTopic
                                setExpandTopic(topic)
                            }
                        }
                    }

                    item { Spacer(Modifier.height(32.dp)) }


                    item { Header(title = stringResource(R.string.task)) }

                    if (tasks.isEmpty()) {
                        item {
                            TextButton(onClick = {
                                tasks.clear()
                            }) {
                                Text(text = stringResource(R.string.add_task))
                            }
                        }
                    } else {
                        items(tasks) { task ->
                            TaskRow(task, onRemove = {
                                tasks.remove(task)
                            })
                        }
                    }
                }
            }

            EditMessage(editMessageShown)
        },
    )
}


@Composable
fun HomeTabBar(bgColor: Color, tabPage: TabPage, onTabSelected: (TabPage) -> Unit) {
    TabRow(selectedTabIndex = tabPage.ordinal,
        modifier = Modifier.background(bgColor),
        indicator = { tabPosition ->
            HomeTabIndicator(tabPosition, tabPosition)
        }) {
        HomeTopTab(TabPage.Home, onTabClick = onTabSelected)

        HomeTopTab(TabPage.Work, onTabClick = onTabSelected)
    }
}


@Composable
fun HomeTopTab(tab: TabPage, modifier: Modifier = Modifier, onTabClick: (TabPage) -> Unit) {
    Button(modifier = modifier.height(tab.tabHeight), onClick = {
        onTabClick(tab)
    }) {

    }
}

@Composable
fun Header(title: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
    }
}


@Composable
fun LoadingRow() {

}

@Composable
fun WeatherRow(onRefresh: () -> Unit) {
    Row {
        Icon(imageVector = Icons.Default.WbSunny, contentDescription = "")
    }
}


@Composable
fun HomeFloatingActionButton(extend: Boolean, onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) { }
}


@Composable
fun EditMessage(shown: Boolean) {
    AnimatedVisibility(visible = shown) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.secondary,
            shadowElevation = 4.dp
        ) {
            Text(
                text = stringResource(R.string.edit_message), modifier = Modifier.padding(16.dp)
            )
        }
    }
}