package com.example.composedemo.chapter9_navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector


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
}


@Composable
fun OverviewBody() {
    Text("OverviewBody")
}

@Composable
fun AccountsBody() {
    Text("AccountsBody")
}

@Composable
fun BillsBody() {
    Text("BillsBody")
}