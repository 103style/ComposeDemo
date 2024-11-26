package com.example.composedemo.chapter5_state

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/**
 * @author Created by kempluo 2024/11/26 14:23
 * CompositionLocal 是实现隐式传参
 *
 *  通过 compositionLocalOf 或者 staticCompositionLocalOf 创建 ProvidableCompositionLocal
 *  staticCompositionLocalOf 的值更改会导致提供CompositonLocal的整个Content被重组，而不仅仅是在组合中读取current值的组件
 *
 *  ⚠️⚠️⚠️
 *  如果 CompositionLocal提供的值发生更改的可能性微乎其微或永远不改变，使用 staticCompositionLocalOf 可以提供性能
 *  ⚠️⚠️⚠️
 */
@Composable
fun CompositionLocalDemo() {
    Column {
        val (elevation, setElevation) = remember {
            mutableStateOf(CardElevation.low)
        }
        CompositionLocalProvider(LocalElevations.provides(CardElevation.medium)) {
            MyCard {}
            println("CompositionLocalDemo - medium")
        }

        Spacer(Modifier.height(8.dp))

        CompositionLocalProvider(LocalElevations.provides(elevation)) {
            MyCard {}
            println("CompositionLocalDemo - $elevation")

        }

        TextButton(onClick = {
            setElevation(CardElevation.high) // 会让使用 elevation 的组件会刷新
        }) {
            Text("change local")
        }

    }
}


data class Elevations(val card: Dp = 0.dp, val bgColor: Color = Color.Red)

val LocalElevations = staticCompositionLocalOf { Elevations() }

object CardElevation {
    val high = Elevations(card = 10.dp, Color.Yellow.copy(alpha = 0.2f))
    val medium = Elevations(card = 5.dp, Color.Green.copy(alpha = 0.2f))
    val low = Elevations(card = 1.dp, Color.Red.copy(alpha = 0.2f))
}


@Composable
private fun MyCard(
    elevation: Dp = LocalElevations.current.card, // ⚠️⚠️⚠️注意这里的使用⚠️⚠️⚠️
    bgColor: Color = LocalElevations.current.bgColor, content: @Composable () -> Unit
) {
    Card(
        elevation = elevation,
        modifier = Modifier.size(200.dp),
        content = content,
        backgroundColor = bgColor
    )
}