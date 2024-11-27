package com.example.composedemo.chapter6_anim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
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
                Box(Modifier.safeDrawingPadding()) {
                    Content()
                }
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

    val (extend, setExtend) = rememberSaveable {
        mutableStateOf(false)
    }

    val (weather, setWeather) = rememberSaveable {
        mutableStateOf(Weather.Sunny)
    }
    val (expandedTopic, setExpandTopic) = rememberSaveable {
        mutableStateOf<DataItem?>(null)
    }
    val (editMessageShown, setEditShow) = rememberSaveable {
        mutableStateOf(false)
    }

    val topics = testTopics

    val tasks = remember {
        mutableStateListOf(
            DataItem(Icons.Default.Task, "Learn Compose Anim"),
            DataItem(Icons.Default.Task, "Learn Compose Gesture"),
            DataItem(Icons.Default.Task, "Learn Compose Navigation"),
            DataItem(Icons.Default.Task, "Learn Compose Integrated"),
            DataItem(Icons.Default.Task, "Learn Compose Anim"),
            DataItem(Icons.Default.Task, "Learn Compose Gesture"),
            DataItem(Icons.Default.Task, "Learn Compose Navigation"),
            DataItem(Icons.Default.Task, "Learn Compose Integrated")
        )
    }

    suspend fun loadWeather() {
        if (!weatherLoading) {
            setWeatherLoading(true)
            delay(1000L)
            setWeather(Weather.entries.toTypedArray().random())
            setWeatherLoading(false)
        }
    }

    suspend fun showEditMessage() {
        if (!editMessageShown) {
            setEditShow(true)
            setExtend(true)
            delay(3000L)
            setEditShow(false)
            setExtend(false)
        }
    }

    // todo LaunchedEffect?
    LaunchedEffect(Unit) {
        loadWeather()
    }


    // todo 1: 点击tab 背景颜色切换动画， 使用 animateColorAsState 来设置背景
    val bgColor by animateColorAsState(tab.bgColor, label = "")

    Scaffold(
        topBar = {
            HomeTabBar(bgColor = bgColor, tabPage = tab, onTabSelected = {
                setTab(it)
            })
        },
        containerColor = bgColor, contentColor = bgColor,
        floatingActionButton = {
            HomeFloatingActionButton(extend = extend, onClick = {
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
                                WeatherRow(weather, onRefresh = {
                                    coroutineScope.launch {
                                        loadWeather()
                                    }
                                })
                            }
                        }
                    }
                    item { Spacer(Modifier.height(16.dp)) }
                    item { Header(title = stringResource(R.string.topics)) }
                    item { Spacer(Modifier.height(16.dp)) }

                    items(topics) { topic ->
                        TopicRow(item = topic, expanded = expandedTopic == topic, onClick = {
                            if (expandedTopic != topic) {
                                setExpandTopic(topic)
                            } else {
                                setExpandTopic(null)
                            }
                        })
                    }

                    item { Spacer(Modifier.height(16.dp)) }
                    item { Header(title = stringResource(R.string.task)) }
                    item { Spacer(Modifier.height(16.dp)) }

                    if (tasks.isEmpty()) {
                        item {
                            Row(modifier = Modifier
                                .clickable {
                                    tasks.clear()
                                }
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 16.dp, vertical = 8.dp)) {
                                Text(text = stringResource(R.string.add_task), color = Color.Black)
                            }
                        }
                    } else {
                        items(count = tasks.size) { index ->
                            // TODO: task类型为啥是list???
                            val task = tasks.getOrNull(index)
                            if (task != null) {
                                TaskRow(task, onRemove = {
                                    tasks.remove(task)
                                })
                            }
                        }
                    }
                }

                EditMessage(editMessageShown)
            }
        },
    )
}


@Composable
fun HomeTabBar(bgColor: Color, tabPage: TabPage, onTabSelected: (TabPage) -> Unit) {
    TabRow(
        selectedTabIndex = tabPage.ordinal, modifier = Modifier.background(bgColor),
        indicator = { tabs ->
            HomeTabIndicator(tabs, tabPage.ordinal)
        },
    ) {
        HomeTopTab(
            TabPage.Home,
            onTabClick = onTabSelected,
            modifier = Modifier.background(color = tabPage.bgColor)
        )

        HomeTopTab(
            TabPage.Work,
            onTabClick = onTabSelected,
            modifier = Modifier.background(color = tabPage.bgColor)
        )
    }
}

@Composable
fun HomeTabIndicator(tabs: List<TabPosition>, index: Int) {
    Box(
        Modifier
            .tabIndicatorOffset(tabs[index])
            .fillMaxHeight()
            .background(color = Color.Gray.copy(alpha = 0.3f))
    )
}


@Composable
fun HomeTopTab(tab: TabPage, modifier: Modifier = Modifier, onTabClick: (TabPage) -> Unit) {
    Row(
        modifier = modifier
            .height(64.dp)
            .clickable {
                onTabClick(tab)
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = tab.icon, contentDescription = null
        )
        Spacer(Modifier.width(8.dp))
        Text(tab.text, style = MaterialTheme.typography.titleMedium, color = Color.White)
    }
}

@Composable
fun Header(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = Color.Black,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}


@Composable
fun LoadingRow() {

}

@Composable
fun WeatherRow(weather: Weather, onRefresh: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = weather.icon, contentDescription = ""
        )

        Spacer(Modifier.width(8.dp))

        Text(weather.temperature, modifier = Modifier.weight(1f))

        IconButton(modifier = Modifier.wrapContentWidth(Alignment.End), onClick = onRefresh) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
        }
    }
}


@Composable
fun TopicRow(item: DataItem, expanded: Boolean, onClick: () -> Unit) {
    // TODO: 3 文本展开的动画  通过设置 animateContentSize 来实现内容的弹开的动画
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onClick() }
            .animateContentSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Icon(imageVector = item.icon, contentDescription = null)

        Spacer(Modifier.width(8.dp))

        Column {
            Text(
                item.title, maxLines = if (expanded) {
                    Int.MAX_VALUE
                } else {
                    1
                }, textAlign = TextAlign.Left, color = Color.Black
            )

            if (expanded && item.detailInfo.isNotBlank()) {
                Text(
                    item.detailInfo, textAlign = TextAlign.Left, color = Color.Black
                )
            }
        }
    }
}

@Composable
fun TaskRow(item: DataItem, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onRemove() },
    ) {
        Icon(imageVector = item.icon, contentDescription = null)

        Spacer(Modifier.width(8.dp))

        Text(item.title, textAlign = TextAlign.Left, color = Color.Black)
    }
}

@Composable
fun HomeFloatingActionButton(extend: Boolean, onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
            )
            // TODO: 2-1.通过 AnimatedVisibility 来为文本的显示隐藏添加动画
            AnimatedVisibility(extend) {
                Text(
                    text = stringResource(R.string.edit),
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.White
                )
            }
        }

    }
}


@Composable
fun EditMessage(shown: Boolean) {
    // TODO: 2-2  自定义显示/隐藏的动画， 向下平移进来， 然后在向上平移出去
    AnimatedVisibility(
        visible = shown, enter = slideInVertically(
            // 通过从偏移量 -fullHeight 像下滑动到0 来进入
            initialOffsetY = { fullHeight -> -fullHeight }, animationSpec = tween(
                durationMillis = 150, easing = LinearOutSlowInEasing
            )
        ),
        // 通过从偏移量 0 像上滑动到 -fullHeight 来退出
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing)
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondary,
            shadowElevation = 4.dp
        ) {
            Text(
                text = stringResource(R.string.edit_message), modifier = Modifier.padding(16.dp)
            )
        }
    }
}