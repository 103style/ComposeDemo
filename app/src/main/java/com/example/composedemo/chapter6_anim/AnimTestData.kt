package com.example.composedemo.chapter6_anim

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Dry
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.composedemo.ui.theme.Pink80
import com.example.composedemo.ui.theme.Purple80

enum class TabPage(
    val text: String, val icon: ImageVector, val bgColor: Color
) {
    Home("Home", Icons.Default.Home, Pink80), Work(
        "Work", Icons.Default.Work, Purple80
    ),
}

enum class Weather(val icon: ImageVector, val temperature: String) {
    Sunny(Icons.Default.WbSunny, "26°C"), Snow(Icons.Default.AcUnit, "-6°C"),
}

val testTopics = listOf(
    DataItem(Icons.Default.AccessAlarm, "111111111111"),
    DataItem(
        Icons.Default.Book,
        "111111111111",
        "撒大家回家卡圣诞季卡上的空间啊合适的借口还是抠脚大汉看到啥可降低环境卡圣诞季卡和思考的机会"
    ),
    DataItem(
        Icons.Default.Book,
        "22222222",
        "adajhfskl啊深刻的哈萨克发哈口角是非卡沙发客健身房卡上发生看见复活卡健身房卡及时反馈"
    ),
    DataItem(
        Icons.Default.Crop,
        "333333333333",
        "哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈"
    ),
    DataItem(
        Icons.Default.Dry,
        "444444444444",
        "哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈"
    ),
    DataItem(
        Icons.Default.Event,
        "555555555555",
        "哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈"
    ),
)
val testTasks = listOf(
    DataItem(Icons.Default.Task, "Learn Compose Anim"),
    DataItem(Icons.Default.Task, "Learn Compose Gesture"),
    DataItem(Icons.Default.Task, "Learn Compose Navigation"),
    DataItem(Icons.Default.Task, "Learn Compose Integrated")
)

data class DataItem(val icon: ImageVector, val title: String, val detailInfo: String = "")
