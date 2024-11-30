package com.example.composedemo.chapter10_effect_api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


/**
 * @author Created by kempluo 2024/11/30 20:46
 *
 * produceState 可以将非Compose状态(Flow、LiveData、RxJava) 转换为 Compose状态。
 * 它接收一个lambda表达式作为函数体，能将这些入参经过一些操作后 生成一个State类型变量 并返回。
 *
 * produceState 创建了一个协程，但它可用于观察非挂起的数据源。
 * 当produceState进入Composition时，获取数据的任务被启动，当其离开Compostion时，该任务被取消。
 *
 */
@Composable
fun ProduceStateDemo() {

    var index by remember {
        mutableIntStateOf(0)
    }

    val list = remember {
        mutableStateListOf("url111111").apply {
            add("url222222")
            add("url333333")
            add("url444444")
        }
    }

    val imageLoad = ImageLoad()
    val url = list.getOrNull(index)

    // 将非Compose状态 转换为 Compose状态。
    val imageResultState = loadNetworkImage(url, imageLoad)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                // 更新index的时候 触发重新loadNetworkImage 然后重新刷新UI
                index = (index + 1) % list.size
            }) {
                Text("选择第${index + 1}张图片")
            }
            Spacer(Modifier.height(16.dp))

            when (imageResultState.value) {
                is Result.Error -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Rounded.Warning, contentDescription = null)
                        Spacer(Modifier.height(8.dp))
                        Text(text = "image load fail")
                    }
                }

                is Result.Success -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Rounded.Warning, contentDescription = null)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = (imageResultState.value as Result.Success).imageResult?.resultUrl
                                ?: "empty result"
                        )
                    }
                }

                else -> {
                    CircularProgressIndicator() //进度条
                }
            }
        }
    }
}

@Composable
fun loadNetworkImage(url: String?, imageLoad: ImageLoad): State<Result<Image>> {
    return produceState(initialValue = Result.Loading as Result<Image>, url, imageLoad) {
        println("----thread:${Thread.currentThread().name}, url:$url")
        // 假设是耗时操作
        val resImage = imageLoad.loadImage(url)
        value = if (resImage == null) {
            Result.Error
        } else {
            Result.Success(resImage)
        }
    }
}

data class Image(val resultUrl: String)

sealed class Result<T> {
    data object Loading : Result<Image>()
    data object Error : Result<Image>()
    data class Success(val imageResult: Image?) : Result<Image>()
}

class ImageLoad {
    suspend fun loadImage(url: String?): Image? {
        return withContext(Dispatchers.IO) {
            try {
                delay(1000)
                Image(url ?: "")
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

}

