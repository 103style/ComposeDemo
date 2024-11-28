package com.example.composedemo.chapter7_gesture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composedemo.ui.theme.ComposeDemoTheme

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
        NestedScrollDemo()
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