package com.example.composedemo.chapter4_customlayout

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

/**
 * @author Created by kempluo 2024/11/23 22:55
 * compose 使用 ConstraintLayout 示例
 *
 * 是用 createRefs 或者 createRefFor 创建引用， ConstraintLayout中的每个可组合项都需要有与之关联的引用
 * 约束条件通过 Modifier.constrainAs(ref) 来设置
 * 约束条件是使用 linkTo() 等有用的方法制定
 * parent是一个现有引用，指向的是 ConstraintLayout 本身
 */
@Composable
fun ConstraintLayoutDemo() {
    ConstraintLayout {
        // createRefs 创建引用， ConstraintLayout 每一个view都要关联一个引用
        val (button, text) = createRefs()
        Button(
            onClick = {},
            // 使用 Modifier.constrainAs 来提供约束，引用作为它的第一个参数
            modifier = Modifier.constrainAs(button) {
                // parent 是 ConstraintLayout 的引用
                top.linkTo(parent.top, margin = 16.dp)
            },
        ) {
            Text("Button")
        }

        Text("text", modifier = Modifier.constrainAs(text) {
            top.linkTo(button.bottom, margin = 16.dp)
            centerHorizontallyTo(parent)
        })
    }
}

@Composable
fun ConstraintLayoutDemo2() {
    ConstraintLayout {
        // createRefs 创建引用， ConstraintLayout 每一个view都要关联一个引用
        val (button1, button2, text) = createRefs()
        Button(
            onClick = {},
            // 使用 Modifier.constrainAs 来提供约束，引用作为它的第一个参数
            modifier = Modifier.constrainAs(button1) {
                // parent 是 ConstraintLayout 的引用
                top.linkTo(parent.top, margin = 16.dp)
            },
        ) {
            Text("Button 1")
        }

        Text("text", modifier = Modifier.constrainAs(text) {
            top.linkTo(button1.bottom, margin = 16.dp)
            centerAround(button1.end)
        })

        // button1 和 text 组成起来， 建立一个屏障（barrier）
        val barrier = createEndBarrier(button1, text)
        Button(
            onClick = {},
            modifier = Modifier.constrainAs(button2) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(barrier)
            },
        ) {
            Text("Button 2")
        }
    }
}

@Composable
fun ConstraintLayoutDemo3() {
    ConstraintLayout {
        val text = createRef()
        // 1/3位置
        val guideline = createGuidelineFromStart(fraction = 1f / 3)

        Text("this is long sentences，hahahahahhahahahahahahahahhahahahahahahhahahahahahahahahhahahahahahahhahahahahahahahahhaha",
            modifier = Modifier.constrainAs(text) {
                linkTo(start = guideline, end = parent.end)
                // 设置自动换行
                width = Dimension.preferredWrapContent
            })

    }
}