package com.example.composedemo.chapter10_effect_api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * @author Created by kempluo 2024/11/30 18:37
 *
 * SideEffect 是简化版的 DisposableEffect，
 * SideEffect 并不接受任何key值，所以每次重组都会执行block。
 * 当不需要 onDispose、不需要参数控制时使用 SideEffect。
 * SideEffect 主要用来 与非Compose管理的对象共享Compose状态。
 * SideEffect 在组合函数 被创建并载入视图树后 才会被调用。
 */
@Composable
fun SideEffectDemo() {
    val index = remember {
        mutableIntStateOf(0)
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {
            index.intValue++
        }) {
            val show = "name:${index.intValue}, click to update"
            Text(show)

            // 更新非UI组件？
            updateUser(User(name = show))
        }
    }
}

@Composable
fun updateUser(newUser: User): User {
    val user = remember {
        User("")
    }
    SideEffect {
        user.updateSomeThing(newUser)
        println("----- SideEffect run")
    }
    return user
}


data class User(var name: String) {

    fun updateSomeThing(value: User) {
        name = value.name
        //...
    }

}