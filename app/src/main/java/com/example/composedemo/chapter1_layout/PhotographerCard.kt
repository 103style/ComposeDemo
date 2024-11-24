package com.example.composedemo.chapter1_layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composedemo.R

/**
 * ⚠️⚠️⚠️
 * · Compose 的 Modifier 默认有先后顺序
 * ⚠️⚠️⚠️
 *
 * Row 用于水平排列子组件，而 Column 用于垂直排列子组件。
 *  Row   相当于 LinearLayout 的 orientation 是 Horizontal
 * Column 相当于 LinearLayout 的 orientation 是 Vertical
 */
@Composable
fun PhotographerCard(modify: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable(onClick = {})
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        ) {
            Image(
                painter = painterResource(R.drawable.user_avatar), contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(text = "Ponny Ma", fontWeight = FontWeight.Bold)
            // https://stackoverflow.com/questions/72574071/unable-to-change-text-emphasis-using-localcontentalpha-in-material-design-3
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                Text(
                    text = "the owner or Tencent company.",
                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
fun PreView() {
    PhotographerCard()
}