package com.example.composedemo.chapter5_state

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.composedemo.R
import java.util.UUID

val defaultDataList = mutableListOf(
    TodoItem("Quick Start", TodoIcon.Done),
    TodoItem("Compose Layout ", TodoIcon.Done),
    TodoItem("Compose State ", TodoIcon.Square),
    TodoItem("Build dynamic UIs ", TodoIcon.Event),
)

data class TodoItem(
    val task: String, val icon: TodoIcon = TodoIcon.Square, val id: UUID = UUID.randomUUID()
)

enum class TodoIcon(
    val imageVector: ImageVector,
    @StringRes val contentDescription: Int,
    val defaultWidth: Dp = 36.dp
) {
    Square(
        Icons.Default.CropSquare, R.string.icon_des_square
    ),
    Done(
        Icons.Default.Done, R.string.icon_des_done
    ),
    Event(Icons.Default.Event, R.string.icon_des_event), Privacy(
        Icons.Default.PrivacyTip, R.string.icon_des_privacy
    ),
    Trash(Icons.Default.RestoreFromTrash, R.string.icon_des_trash), ;

}


fun generateRandomTodoItem(): TodoItem {
    val msg = listOf(
        "Learn compose",
        "Learn state",
        "Build dynamic UIs",
        "Learn Unidirectioal Data Flow",
        "Integrate LiveData",
        "Integrate ViewModle",
        "Remember to savedState",
        "Build stateless composables",
        "Use state from stateless composables",
        "Ha Ha Ha Ha Ha Ha Ha Ha Ha Ha Ha Ha Ha Ha Ha Ha Ha Ha Ha Ha Ha",
    ).random()
    val icon = TodoIcon.values().random()
    return TodoItem(msg, icon)
}