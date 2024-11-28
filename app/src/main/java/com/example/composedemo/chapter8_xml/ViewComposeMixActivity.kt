package com.example.composedemo.chapter8_xml

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import com.example.composedemo.R

/**
 * @author Created by kempluo 2024/11/28 22:47
 *
 * View 和 Compose 互相混用
 * xml中用 Compose 可以使用 <androidx.compose.ui.platform.ComposeView/>
 * @see ContentView
 *
 * Compose中使用View 是用 AndroidView组件，通过 factory组件View， 然后通过 update更新View
 * @see ComposeUseView
 *
 * Compose 用 style中的主题 可以使用 MdcTheme. 不过已经废弃了🐶
 * 需要添加依赖 com.google.android.material:compose-theme-adapter
 */
class ViewComposeMixActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_use_compose)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // View 中使用 Compose
        findViewById<ComposeView>(R.id.content).setContent {
            // TODO:  Compose中使用 style中的主题, 好像用不用也没什么区别？？？
//            MdcTheme {
            ContentView()
//            }
        }
    }
}

@Composable
private fun ContentView() {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f))
    ) {
        ComposeUseView()


        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            repeat(32) {
                Box(
                    Modifier
                        .heightIn(min = 64.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Item:$it")
                }

            }
        }
    }
}

/**
 * Compose中使用View
 */
@Composable
private fun ComposeUseView() {
    AndroidView(factory = { context ->
        TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(context.resources.getDimensionPixelSize(R.dimen.testview_padding))
        }
    }, update = {
        it.text = "我是Compose 中使用的TextView"
    })
}
