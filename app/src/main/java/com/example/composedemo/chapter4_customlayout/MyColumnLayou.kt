package com.example.composedemo.chapter4_customlayout

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import com.example.composedemo.ui.theme.ComposeDemoTheme

/**
 * 自己实现Column布局
 */
@Composable
fun MyColumnLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(content, modifier = modifier) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        var y = 0
        // 布局的大小
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach { placeable ->
                // 设置当前元素的高度
                placeable.placeRelative(0, y)
                // 更新下一行开始的y坐标
                y += placeable.height
            }
        }
    }
}


@Composable
fun MyColumnLayoutDemo() {
    ComposeDemoTheme {
        MyColumnLayout {
            Text("MyColumnLayout")
            Text("places items")
            Text("vertically")
            Text("We've done it by hand.")
        }
    }
}