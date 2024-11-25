package com.example.composedemo.chapter5_state

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StateSaveDemo() {
    // 指定mapSaver 用于储存是如何构建map对象，获取是如果恢复对象
    val cityMapSaver = run {
        val nameKey = "Name"
        val countryKey = "Country"
        mapSaver(save = { mapOf(nameKey to it.name, countryKey to it.country) },
            restore = { City(it[nameKey] as String, it[countryKey] as String) })
    }

    // listSaver
    val cityListSaver = listSaver<City, Any>(save = { listOf(it.name, it.country) },
        restore = { City(it[0] as String, it[1] as String) })

    // rememberSaveable 能保证横竖屏切换等状态 修改的值不会变为初始值
    val (city, setCity) = rememberSaveable(stateSaver = cityMapSaver) {
//    val (city, setCity) = rememberSaveable(stateSaver = cityListSaver) {
        mutableStateOf(City("Madrid", "Spain"))
    }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(12.dp)) {
        TextButton(onClick = {
            setCity(City("Beijing", "China"))
        }, colors = ButtonDefaults.buttonColors()) {
            Text("Click to change")
        }

        Text("${city.name} - ${city.country}")
    }
}

data class City(val name: String, val country: String)