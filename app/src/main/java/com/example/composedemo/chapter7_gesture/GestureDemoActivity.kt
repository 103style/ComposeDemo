package com.example.composedemo.chapter7_gesture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.composedemo.ui.theme.ComposeDemoTheme
import kotlin.math.roundToInt

/**
 * @author Created by kempluo 2024/11/28 14:17
 * 手势处理
 */
class GestureDemoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                Box(Modifier.safeDrawingPadding()) {
                    GestureDemo()
                }
            }
        }
    }
}

@Composable
fun GestureDemo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
//        ClickableDemo()

//        ScrollBoxes()
//        ScrollBoxesSmooth()
//        ScrollableDemo()
//        NestedScrollDemo()

//        DraggableDemo()
//        DraggableWhereYouWant()

//        SwipeableDemo()

        TransformableDemo()
    }
}

@Composable
fun ClickableDemo() {
    val count = remember {
        mutableIntStateOf(0)
    }
    Text(
        text = count.value.toString(), textAlign = TextAlign.Center,
        modifier = Modifier
//            .clickable {
//                count.value += 1
//            }
            .pointerInput(Unit) {
                detectTapGestures(onPress = { println("-----onPress") },
                    onDoubleTap = { println("-----onDoubleTap") },
                    onLongPress = { println("-----onLongPress") },
                    onTap = { println("-----onTap") })
            }
            .wrapContentSize()
            .background(Color.LightGray)
            .padding(horizontal = 50.dp, vertical = 40.dp),
    )
}


/**
 * 滚动修饰符
 * 配置 .verticalScroll(rememberScrollState()) 可以实现滑动
 */
@Composable
fun ScrollBoxes() {
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .size(100.dp)
            .verticalScroll(rememberScrollState())
    ) {
        repeat(10) {
            Text("Item:%$it", modifier = Modifier.padding(2.dp))
        }
    }
}


/**
 * 自己调用 ScrollState 的 滚动函数
 */
@Composable
fun ScrollBoxesSmooth() {
    val state = rememberScrollState()
    // 附带效应？
    LaunchedEffect(Unit) {
        state.animateScrollTo(500) //  滑动到 y=500的位置
    }
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .size(100.dp)
            .verticalScroll(state)
    ) {
        repeat(10) {
            Text("Item:%$it", modifier = Modifier.padding(2.dp))
        }
    }
}

/**
 * 可滚动修饰符
 * scrollable 修饰符与滚动修饰符不同。区别在于scrollable 可检测滚动手势， 但不会偏移起内容
 */
@Composable
fun ScrollableDemo() {
    var offset by remember { mutableFloatStateOf(0f) }
    Box(
        Modifier
            .size(150.dp)
            .scrollable(orientation = Orientation.Vertical,
                state = rememberScrollableState { delta -> // 滚动的偏移量
                    offset += delta
                    delta
                })
            .background(Color.LightGray), contentAlignment = Alignment.Center
    ) {
        Text(text = offset.toString())
    }
}

/**
 * 嵌套滚动
 *
 * 内部 box 滑动完成之后 再继续 column的滑动
 */
@Composable
fun NestedScrollDemo() {
    val gradient = Brush.verticalGradient(0f to Color.Gray, 1000f to Color.White)
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .verticalScroll(rememberScrollState())
            .padding(32.dp), contentAlignment = Alignment.Center
    ) {
        Column {
            repeat(6) {
                Box(
                    modifier = Modifier
                        .height(128.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Item:$it",
                        modifier = Modifier
                            .border(12.dp, Color.DarkGray)
                            .background(brush = gradient)
                            .padding(24.dp)
                            .height(150.dp) //
                    )
                }
            }
        }
    }
}

/**
 * 拖动
 * .draggable(orientation = Orientation.Horizontal) 只能单方向
 */
@Composable
fun DraggableDemo() {
    var offsetX by remember { mutableFloatStateOf(0f) }
    Text(text = "Drag me",
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .draggable(orientation = Orientation.Horizontal,
                state = rememberDraggableState { offsetX += it })

    )
}

/**
 * 随意拖动
 */
@Composable
fun DraggableWhereYouWant() {
    Box(modifier = Modifier.fillMaxSize()) {
        var offsetX by remember { mutableFloatStateOf(0f) }
        var offsetY by remember { mutableFloatStateOf(0f) }

        Box(
            Modifier
                .offset { IntOffset(x = offsetX.roundToInt(), y = offsetY.roundToInt()) }
                .background(Color.Blue)
                .size(50.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                })
    }
}


/**
 * 惯性滑动
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableDemo() {
    val width = 200.dp
    val squareSize = 100.dp

    val swipeableState = rememberSwipeableState(0)

    val sizePx = with(LocalDensity.current) {
        squareSize.toPx()
    }
    val anchors = mapOf(0f to 0, sizePx to 1)
    Box(
        Modifier
            .width(width)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .background(Color.LightGray)
    ) {
        Box(
            Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .size(squareSize)
                .background(Color.DarkGray))
    }
}

/**
 * 多点触控  平移、缩放、旋转
 * 可以使用 transformable 修饰符，只会检测手势
 */
@Composable
fun TransformableDemo() {
    var scale by remember { mutableFloatStateOf(1f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }
    Box(
        Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                rotationZ = rotation
                translationX = offset.x
                translationY = offset.y
            }
            .transformable(state)
            .background(Color.Blue)
            .size(200.dp, 100.dp),
        contentAlignment = Alignment.Center) {
        Text(
            text = "安装平移、缩放、旋转 操作，模拟器笔记本按住control键操作",
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )
    }
}