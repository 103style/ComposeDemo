package com.example.composedemo.chapter6_anim

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Dry
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class TabPage(
    val text: String, val icon: ImageVector, val bgColor: Color, val tabHeight: Dp = 64.dp
) {
    Home("Home", Icons.Default.Home, Color.Red), Work("Work", Icons.Default.Work, Color.Cyan)
}

enum class Weather(val icon: ImageVector, val temperature: String) {
    Sunny(Icons.Default.WbSunny, "26°C"), Snow(Icons.Default.AcUnit, "-6°C"),
}

val testTopics = listOf(
    DataItem(Icons.Default.AccessAlarm, "111111111111"),
    DataItem(
        Icons.Default.Book,
        "撒大家回家卡圣诞季卡上的空间啊合适的借口还是抠脚大汉看到啥可降低环境卡圣诞季卡和思考的机会"
    ),
    DataItem(Icons.Default.Crop, "333333333333"),
    DataItem(Icons.Default.Dry, "444444444444"),
    DataItem(Icons.Default.Event, "555555555555"),
)
val testTasks = mutableListOf<DataItem>().apply {
    addAll(testTopics)
}

data class DataItem(val icon: ImageVector, val title: String)

