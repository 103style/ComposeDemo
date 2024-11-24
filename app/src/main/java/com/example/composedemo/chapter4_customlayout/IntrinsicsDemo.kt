package com.example.composedemo.chapter4_customlayout

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * @author Created by kempluo 2024/11/24 15:03
 *
 * Compose只测量一次子元素
 */
@Composable
fun TwoTexts(modifier: Modifier = Modifier) {
    // IntrinsicSize.Min == wrap_content
    Row(modifier.height(IntrinsicSize.Min)) {
        Text(
            "Hi",
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start)
        )

        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = Color.Black,
        )
        Text(
            "three",
            modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.End)
        )
    }
}