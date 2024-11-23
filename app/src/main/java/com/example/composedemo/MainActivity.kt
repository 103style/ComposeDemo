package com.example.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                LazyColumn {
                    items(10) {
                        MessageCard(
                            "Jack" + it,
                            "Android Developer and keeper. this is detail info test message."
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Message(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    isExpanded: Boolean = false
) {
    Text(
        text, modifier = modifier, color = color, maxLines = if (isExpanded) {
            Int.MAX_VALUE
        } else {
            1
        }
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
        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor: Color by animateColorAsState(
            if (isExpanded) {
                Color.Red
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Spacer(modifier = Modifier.width(4.dp))
            Message(
                name,
                Modifier.background(MaterialTheme.colorScheme.background),
                MaterialTheme.colorScheme.primary,
                isExpanded = isExpanded
            )
            Spacer(modifier = Modifier.width(4.dp))
            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                tonalElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Message(
                    des,
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(bottom = 4.dp),
                    isExpanded = isExpanded
                )
            }
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