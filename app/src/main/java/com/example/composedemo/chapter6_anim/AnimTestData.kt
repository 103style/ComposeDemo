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
    Home("Home", Icons.Default.Home, Color.Red), Work(
        "Work", Icons.Default.Work, Color.Green
    ),
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
    DataItem(
        Icons.Default.Book,
        "adajhfskl啊深刻的哈萨克发哈口角是非卡沙发客健身房卡上发生看见复活卡健身房卡及时反馈"
    ),
    DataItem(
        Icons.Default.Book,
        "啊是极其恶劣分局开会胃口好起来然后开启五花肉块钱把我加入吧俺可大可久请回复开启和我客气好看"
    ),
    DataItem(
        Icons.Default.Book,
        "啊是大客户反馈千家万户期间还为u却很委屈我呢群空间微博能看清就为看剧情稳步前进额不进去我崩溃就去吧"
    ),
    DataItem(Icons.Default.Crop, "333333333333"),
    DataItem(Icons.Default.Dry, "444444444444"),
    DataItem(Icons.Default.Event, "555555555555"),
)
val testTasks = mutableListOf<DataItem>().apply {
    addAll(testTopics)
}

data class DataItem(val icon: ImageVector, val title: String)
