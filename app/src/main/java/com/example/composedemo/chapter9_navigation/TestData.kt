package com.example.composedemo.chapter9_navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign


enum class RallyScreen(val icon: ImageVector, val body: @Composable ((String) -> Unit) -> Unit) {
    Overview(icon = Icons.Filled.PieChart, body = {
        OverviewBody()
    }),
    Accounts(icon = Icons.Filled.AttachMoney, body = {
        AccountsBody()
    }),
    Bills(icon = Icons.Filled.MoneyOff, body = {
        BillsBody()
    });

    @Composable
    fun content(onScreenChange: (String) -> Unit) {
        body(onScreenChange)
    }

    companion object {
        fun formRouter(route: String?): RallyScreen {
            println("------route:${route?.substringBefore("/")}")
            return when (route?.substringBefore("/")) {
                Accounts.name -> Accounts
                Bills.name -> Bills
                Overview.name, null -> Overview
                else -> throw IllegalArgumentException("not support router:$route")
            }
        }
    }
}


@Composable
fun OverviewBody(onclick: () -> Unit = {}) {
    Text("OverviewBody", modifier = Modifier
        .fillMaxSize()
        .clickable {
            onclick.invoke()
        }, textAlign = TextAlign.Center)
}

@Composable
fun AccountsBody(accountName: String? = "") {
    val text = if (accountName.isNullOrBlank()) {
        "AccountsBody"
    } else {
        "AccountsBody, $accountName"
    }
    Text(
        text, modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center
    )
}

@Composable
fun BillsBody() {
    Text("BillsBody", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
}