package com.example.composedemo.chapter4_customlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.composedemo.ui.theme.ComposeDemoTheme

class CustomLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                Box(Modifier.safeDrawingPadding()) {
//                    TextWithPaddingToBaseLine()
//                    MyColumnLayoutDemo() // 自己实现column布局
//                    StaggeredGridDemo() // 自己实现 gridview
//                    ConstraintLayoutDemo() // constraintLayout使用
//                    ConstraintLayoutDemo2() // constraintLayout使用
//                    ConstraintLayoutDemo3() // constraintLayout使用
//                    DecoupleConstraintLayoutDemo() // 固定的配置抽离出来， 变化到通过通过传参配置
                    TwoTexts()
                }
            }
        }
    }
}

/**
 * 修改文本的firstBaseline
 */
fun Modifier.firstBaselineToTop(firstBaselineToTop: Dp) =
    this.then(layout { measurable, constraints ->
        // 测量元素
        val placeable = measurable.measure(constraints)
        // 获取元素基线值
        val firstBaseline = placeable[FirstBaseline]
        // 元素新的Y坐标 传入的baselineToTop - lineToTop
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        // 元素的高度
        val height = placeable.height + placeableY

        // 设置View的位置
        layout(placeable.width, height) {
            // 设置文本元素位置
            placeable.placeRelative(0, placeableY)
        }
    })

/**
 * firstBaselineToTop 实现修改文本的baseLine高度
 */
@Composable
fun TextWithPaddingToBaseLine() {
    ComposeDemoTheme {
        Text(
            text = "let's do it.",
            Modifier
                .firstBaselineToTop(36.dp)
                .background(Color.Red)
        )
    }
}