package com.example.composedemo.chapter2_slotsapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class SlotsApiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScaffoldDemo()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldDemo() {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "ScaffoldDemo") }, actions = {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)
            }
        })
    }) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding))
    }

}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(text = "Hi there!")
        Text(text = "Thanks for going through the Scaffold Study.")
    }
}