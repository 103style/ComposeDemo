package com.example.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composedemo.ui.theme.ComposeDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDemoTheme {
                MessageCard("Jack", "Android Developer and keeper.")
            }
        }
    }
}


@Composable
fun Message(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified) {
    Text(
        text, modifier = modifier, color = color
    )
}

@Composable
fun MessageCardList(name: String, des: String) {
    LazyColumn {
        items(10) {
            MessageCard(name + 1, des)
        }
    }
}

@Composable
fun MessageCard(name: String, des: String) {
    Row(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Spacer(modifier = Modifier.width(4.dp))
            Message(
                name,
                Modifier.background(MaterialTheme.colorScheme.background),
                MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Message(
                des,
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(bottom = 4.dp)
            )
        }
    }
}


@Preview
@Composable
fun MessagePreView() {
    ComposeDemoTheme {
        MessageCardList("Jack", "Android Developer and keeper.")
    }
}