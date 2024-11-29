package com.example.composedemo.chapter9_navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composedemo.ui.theme.ComposeDemoTheme

/**
 * @author Created by kempluo 2024/11/29 14:21
 */
class NavigationDemoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                Box(Modifier.safeDrawingPadding()) {
                    RallyApp()
                }
            }
        }
    }
}

@Composable
fun RallyApp() {
    val allScreens = RallyScreen.values().toList()


    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()

//    var currentScreen by rememberSaveable { mutableStateOf(RallyScreen.Overview) }
    val currentScreen = RallyScreen.formRouter(backStackEntry.value?.destination?.route)

    Scaffold(topBar = {
        RallyTabRow(
            allScreens = allScreens, onTabSelected = { screen ->
//                currentScreen = screen
                navController.navigate(screen.name)
            }, current = currentScreen
        )
    }) { innerPadding ->
        // 非navigation方式
//        Box(Modifier.padding(innerPadding)) {
//            currentScreen.content(onScreenChange = { screen ->
//                currentScreen = RallyScreen.valueOf(screen)
//            })
//        }

        // navigation方式
        RallyNavHost(navController = navController, Modifier.padding(innerPadding))
    }

}

@Composable
fun RallyTabRow(
    allScreens: List<RallyScreen>, onTabSelected: (RallyScreen) -> Unit, current: RallyScreen
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp)
    ) {
        allScreens.forEach {
            RallyTab(it, onClick = {
                onTabSelected(it)
            }, current == it)
        }

    }
}


@Composable
fun RallyTab(info: RallyScreen, onClick: () -> Unit, isSelected: Boolean) {
    Row(modifier = Modifier
        .heightIn(min = 64.dp)
        .clickable {
            onClick()
        }
        .padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = info.icon, contentDescription = "")
        if (isSelected) {
            Spacer(Modifier.width(8.dp))
            Text(info.name)
        }
    }
}


@Composable
fun RallyNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = RallyScreen.Overview.name,
        modifier = modifier
    ) {
        composable(route = RallyScreen.Overview.name) {
            OverviewBody(onclick = {
//                navController.navigate(RallyScreen.Bills.name)
                navigateUseArgument(navController, "testArgument")
            })
        }

        composable(route = RallyScreen.Accounts.name) {
            AccountsBody()
        }

        composable(route = RallyScreen.Bills.name) {
            BillsBody()
        }

        // 带参数的跳转
        composable(
            route = "${RallyScreen.Accounts.name}/{name}", arguments = listOf(
                //定义string类型的参数 name
                navArgument("name") {
                    type = NavType.StringType
                },
            )
        ) { entry ->
            val accountName = entry.arguments?.getString("name")
            AccountsBody(accountName)
        }
    }
}

private fun navigateUseArgument(navController: NavHostController, argument: String) {
    println("---------${RallyScreen.Accounts.name}/$argument")
    navController.navigate("${RallyScreen.Accounts.name}/$argument")
}
