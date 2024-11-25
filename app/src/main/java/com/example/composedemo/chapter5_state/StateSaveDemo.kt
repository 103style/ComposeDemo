package com.example.composedemo.chapter5_state

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StateSaveDemo() {
    val citySaver = run {
        val nameKey = "Name"
        val countryKey = "Country"
        mapSaver(save = { mapOf(nameKey to it.name, countryKey to it.country) },
            restore = { City(it[nameKey] as String, it[countryKey] as String) })
    }

    // rememberSaveable 能保证横竖屏切换等状态 修改的值不会变为初始值
    val (city, setCity) = rememberSaveable(stateSaver = citySaver) {
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