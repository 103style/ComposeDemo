package com.example.composedemo.chapter6_anim

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Work
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class TabPage(
    val text: String,
    val icon: ImageVector,
    val bgColor: Color,
    val tabHeight: Dp = 64.dp
) {
    Home("Home", Icons.Default.Home, Color.Magenta), Work("Work", Icons.Default.Work, Color.Green)
}


val testTopics = arrayListOf<DataItem>()
val testTasks = arrayListOf<DataItem>()

data class DataItem(val icon: ImageVector, val title:String)

