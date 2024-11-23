package com.example.composedemo.chapter4_customlayout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @author Created by kempluo 2024/11/23 22:12
 * 布局方式：把所有元素依次放入下一行，
 * 比如一共 10个元素， rowCount = 3
 * 布局方式如下：
 * 1 4 7 10
 * 2 5 8
 * 3 6 9
 */
@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier, rowCount: Int = 3, content: @Composable () -> Unit
) {
    Layout(content, modifier) { measurables, constraints ->
        val rowWidths = IntArray(rowCount)
        val rowHeights = IntArray(rowCount) { 0 }

        val placeables = measurables.mapIndexed { index, measurable ->
            // 测量每一个元素
            val placeable = measurable.measure(constraints)
            // 计算每一个行的宽高
            val row = index % rowCount
            // 宽度的所有的子view宽度相加
            rowWidths[row] += placeable.width
            // 取最高的高度
            rowHeights[row] = rowHeights[row].coerceAtLeast(placeable.height)

            placeable
        }

        val width = rowWidths.maxOrNull() ?: constraints.minWidth
        val height = rowHeights.sum()

        val rowX = IntArray(rowCount)
        // 每一行的y坐标等于 上一行的Y坐标 + 上一行的最大高度
        val rowY = IntArray(rowCount)
        for (i in 1 until rowCount) {
            rowY[i] = rowY[i - 1] + rowHeights[i - 1]
        }
        layout(width, height) {
            placeables.forEachIndexed { index, placeable ->
                val row = index % rowCount
                placeable.placeRelative(rowX[row], rowY[row])
                rowX[row] += placeable.width
            }
        }
    }
}


@Composable
private fun Item(text: String, modifier: Modifier = Modifier) {
    Card(
        modifier, border = BorderStroke(Dp.Hairline, Color.Black), shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(color = MaterialTheme.colorScheme.secondary)
            ) {

            }
            Spacer(Modifier.size(8.dp))
            Text(text)
        }
    }
}

@Composable
fun StaggeredGridDemo(modifier: Modifier = Modifier) {
    Row(
        modifier
            .background(color = Color.LightGray)
            .padding(16.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        StaggeredGrid(modifier) {
            repeat(16) {
                Item(
                    String.format("%.${(Math.random() * 5).toInt()}f", Math.random()),
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }

}
