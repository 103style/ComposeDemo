package com.example.composedemo.chapter10_effect_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import com.example.composedemo.ui.theme.ComposeDemoTheme

/**
 * @author Created by kempluo 2024/11/30 14:51
 */
class EffectApiDemoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeDemoTheme {
                Box(Modifier.safeDrawingPadding()) {
//                    LaunchEffectDemo()
//                    RememberCoroutineScopeDemo()
//                    RememberUpdatedStateDemo()
//                    DisposableEffectDemo(onBackPressedDispatcher)
                    SideEffectDemo()
                }
            }
        }
    }
}
