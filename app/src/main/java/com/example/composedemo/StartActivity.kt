package com.example.composedemo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composedemo.chapter10_effect_api.EffectApiDemoActivity
import com.example.composedemo.chapter1_layout.LayoutActivity
import com.example.composedemo.chapter2_slotsapi.SlotsApiActivity
import com.example.composedemo.chapter3_list.ListActivity
import com.example.composedemo.chapter4_customlayout.CustomLayoutActivity
import com.example.composedemo.chapter5_state.StateDemoActivity
import com.example.composedemo.chapter6_anim.AnimDemoActivity
import com.example.composedemo.chapter7_gesture.GestureDemoActivity
import com.example.composedemo.chapter8_view_compose.ViewComposeMixActivity
import com.example.composedemo.chapter9_navigation.NavigationDemoActivity
import com.example.composedemo.ui.theme.ComposeDemoTheme

/**
 * @author Created by kempluo 2024/12/1 21:13
 * Demo汇总列表
 */
class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // 设置成全屏模式
        setContent {
            ComposeDemoTheme {
                // safeDrawingPadding 安全绘制边界，
                // 这个修饰符的主要目的是确保 UI 元素不会被系统 UI（如状态栏、导航栏或其他系统界面元素）遮挡，
                // 从而提供更好的用户体验。
                Box(Modifier.safeDrawingPadding()) {
                    LazyColumn {
                        items(10) {
                            ItemDemo(it)
                        }
                    }
                }
            }
        }
    }

    private val demoList = listOf(
        "QuickStartDemo",
        "LayoutDemo",
        "SlotsApiDemo",
        "ListDemo",
        "CustomLayoutDemo",
        "StateDemoDemo",
        "AnimDemoDemo",
        "GestureDemoDemo",
        "ViewComposeMixDemo",
        "NavigationDemoDemo",
        "EffectApiDemoDemo",
    )

    private val demoJumpActList = listOf(
        MainActivity::class.java,
        LayoutActivity::class.java,
        SlotsApiActivity::class.java,
        ListActivity::class.java,
        CustomLayoutActivity::class.java,
        StateDemoActivity::class.java,
        AnimDemoActivity::class.java,
        GestureDemoActivity::class.java,
        ViewComposeMixActivity::class.java,
        NavigationDemoActivity::class.java,
        EffectApiDemoActivity::class.java,
    )

    @Composable
    private fun ItemDemo(index: Int) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .clickable {
                demoJumpActList
                    .getOrNull(index)
                    ?.let {
                        val intent = Intent(this, demoJumpActList[index])
                        startActivity(intent)
                    }
            },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = demoList.getOrNull(index) ?: "")
        }
    }
}



