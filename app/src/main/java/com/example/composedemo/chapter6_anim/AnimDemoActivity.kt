package com.example.composedemo.chapter6_anim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.composedemo.R
import com.example.composedemo.ui.theme.ComposeDemoTheme
import com.example.composedemo.ui.theme.Pink40
import com.example.composedemo.ui.theme.Purple40
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

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
        mutableStateListOf<DataItem>().apply { addAll(testTasks) }
    }

    suspend fun loadWeather() {
        if (!weatherLoading) {
            setWeatherLoading(true)
            delay(3000L)
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
                                .heightIn(min = 64.dp)
                                .clickable {
                                    tasks.clear()
                                    tasks.addAll(testTasks)
                                }
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stringResource(R.string.add_task),
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }
                    } else {
                        items(count = tasks.size) { index ->
                            val task = tasks.getOrNull(index)
                            if (task != null) {
                                // ⚠️⚠️⚠️ key(task) 这个很重要，用来标记唯一标识，⚠️⚠️⚠️
                                // 不加这个 侧滑删除会导致虽然删除了， 但是用到这个位置的后续item不会恢复对应的状态，
                                key(task) {
                                    TaskRow(task, onRemove = {
                                        tasks.remove(task)
                                    })
                                }
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
            HomeTabIndicator(tabs, tabPage)
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
fun HomeTabIndicator(tabPositions: List<TabPosition>, tabPage: TabPage) {
    // TODO: 4. tab选中框的切换动画
    // 自定义选项卡指示器
    // 多值动画：在状态发生改变时，有多个点动画值要一起发生改变
    // 设置一个Transition 并使用 targetState提供的目标对其进行更新。
    // 当targetState更改时，Transition将朝着为新 targetState 指定的目标值运行其所有子动画
    // 可以使用 Transition 动态添加子动画: Transition.animateFloat、animateColor、animateValue 等
    val transition = updateTransition(tabPage, label = "Tab indicator")
    val index = tabPage.ordinal
//    val indicatorLeft = tabPositions[index].left   // 当前选中tab的左侧x坐标
    val indicatorLeft by transition.animateDp(label = "Tab indicator Left", transitionSpec = {
        // 左边切换到右边， 左边缘移动要比右边慢
        if (TabPage.Home isTransitioningTo TabPage.Work) {
            spring(stiffness = Spring.StiffnessVeryLow)
        } else {
            spring(stiffness = Spring.StiffnessMedium)
        }
    }) {
        tabPositions[it.ordinal].left
    }
//    val indicatorRight = tabPositions[index].right // 当前选中tab的右侧x坐标
    val indicatorRight by transition.animateDp(label = "Tab indicator Right", transitionSpec = {
        // 右边切换到左边， 右边缘移动要比左边慢
        if (TabPage.Home isTransitioningTo TabPage.Work) {
            spring(stiffness = Spring.StiffnessMedium)
        } else {
            spring(stiffness = Spring.StiffnessVeryLow)
        }
    }) {
        tabPositions[it.ordinal].right
    }

    val color by transition.animateColor(label = "Tab indicator Color") {
        if (it == TabPage.Home) Purple40 else Pink40
    }

    Box(
        Modifier
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorLeft) // 偏移值 就是选中tab的左侧 x
            .width(indicatorRight - indicatorLeft) // 宽度 为右侧x - 左侧x
            .padding(2.dp)
            .fillMaxSize()
            .border(
                BorderStroke(2.dp, color), RoundedCornerShape(4.dp)
            )
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
    // TODO: 5.重复动画 Animate value between 0f and 1f, then bach to 0f repeatedly.
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                1f at 500 // 500ms 的时候 alpha 是 1f, 也即是 0-500ms的时候执行动画 500-1000的时候维持1f的alpha
            },
            repeatMode = RepeatMode.Reverse,
        ), label = ""
    )
    Row(
        modifier = Modifier
            .heightIn(min = 64.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = alpha))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(Color.LightGray.copy(alpha = alpha))
        )

    }
}

@Composable
fun WeatherRow(weather: Weather, onRefresh: () -> Unit) {
    Row(
        modifier = Modifier
            .heightIn(min = 64.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = weather.icon, contentDescription = "", modifier = Modifier.size(48.dp)
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
    Row(modifier = Modifier
        .heightIn(min = 64.dp)
        .fillMaxWidth()
        .background(Color.White)
        .clickable { onClick() }
        .animateContentSize()
        .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = item.icon, contentDescription = null)

        Spacer(Modifier.width(8.dp))

        Column {
            Text(
                item.title,
                maxLines = if (expanded) {
                    Int.MAX_VALUE
                } else {
                    1
                },
                textAlign = TextAlign.Left,
                color = Color.Black,
                style = MaterialTheme.typography.bodyLarge,
            )

            if (expanded && item.detailInfo.isNotBlank()) {
                Text(
                    item.detailInfo,
                    textAlign = TextAlign.Left,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
fun TaskRow(item: DataItem, onRemove: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .swipeToDismiss(onRemove),
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 64.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon, contentDescription = null
            )

            Spacer(Modifier.width(8.dp))

            Text(
                item.title,
                textAlign = TextAlign.Left,
                color = Color.Black,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
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

// TODO: 6-1 手势动画 来实现侧滑删除
private fun Modifier.swipeToDismiss(onDismissed: () -> Unit): Modifier = composed {
    // 水平滑动动画
    val offsetX = remember { Animatable(0f) }

    pointerInput(Unit) { // 这里可以处理触摸事件 相 当 View的 xxxTouchEvent
        // 滑动手指抬起之后衰减动画， 用于计算动画最后的固定位置
        val decay = splineBasedDecay<Float>(this)

        coroutineScope {
            // 创建一个修饰符，用于处理修改元素区域内的光标输入
            // pointerInput可以调用PionterInputScope.awaitPointerEventScope
            // 以安装可以等待PointEventScope的光标输入处理程序
            while (true) {
                // 等待触摸按下事件
                // awaitPointerEventScope: 挂起并安装指针输入块，该块可以等待输入时间并立即响应他们
                // awaitFirstDown： 读取事件，直到收到第一个down
                val pointerId = awaitPointerEventScope {
                    awaitFirstDown().id // ACTION_DOWN
                }
                val velocityTracker = VelocityTracker()
                // 等待拖动事件
                awaitPointerEventScope {
                    // 监听水平滑动
                    horizontalDrag(pointerId) { change ->
                        val horDragOffset = offsetX.value + change.positionChange().x
                        // 启动协程，执行动画
                        launch {
                            offsetX.snapTo(horDragOffset)
                        }
                        // 记录滑动的位置
                        velocityTracker.addPosition(change.uptimeMillis, change.position)

                        // 消费掉手势事件，而不是传递给外部
                        if (change.positionChange() != Offset.Zero) {
                            change.consume()
                        }
                    }
                }
                // 拖动完成，计算继续衰减滑动的速度
                val velocity = velocityTracker.calculateVelocity().x
                // 计算手抬起之后 还能滑动的距离
                val targetOffset = decay.calculateTargetValue(offsetX.value, velocity)

                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(), upperBound = size.width.toFloat()
                )

                launch {
                    if (targetOffset.absoluteValue <= size.width) {
                        // 没滑出去 就滑回来
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        // 启动衰减动画
                        offsetX.animateDecay(velocity, decay)
                        onDismissed()
                        // ⚠️⚠️⚠️
                        // 如果配置taskRow 不加 key(task)， 调用下面这个函数虽然看起来没问题
                        // 但是 删除了一半的时候就会发现侧滑删除不了了
                        // offsetX.snapTo(0f)
                        // ⚠️⚠️⚠️
                    }
                }
            }
        }
    }.offset {
        IntOffset(offsetX.value.roundToInt(), 0)
    }
}
